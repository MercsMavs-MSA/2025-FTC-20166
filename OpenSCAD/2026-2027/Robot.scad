$fn         = 100;
ShooterAngle = 0;
RampWidth   = 6;//3.8;
PollenD     = 2.8;
Compression = 0.4;
RampT       = 0.25;
PanelT      = 0.125;
MountD      = 3.95 / 25.4;//0.5824;//0.5824 for 0.5 hex shaft
MountShape  = 50;//6 for 0.5 hex shaft

InnerD = 4 + PollenD + PollenD - Compression;
OuterD = 4 + PollenD + PollenD - Compression + RampT;
Width  = RampWidth;

ServoRotateOffset = 9.85;

module RoundedBlock(XDim = 43, YDim = 43, ZDim = 9.6, CurveD = 4)
{
  hull()
  {
    translate([-(XDim - CurveD)/ 2, -(YDim  - CurveD)/ 2, -(ZDim - CurveD) / 2])
      sphere( d = CurveD, $fn = 50);
    translate([(XDim - CurveD)/ 2, -(YDim  - CurveD)/ 2, -(ZDim - CurveD) / 2])
      sphere( d = CurveD, $fn = 50);
    translate([-(XDim - CurveD)/ 2, (YDim  - CurveD)/ 2, -(ZDim - CurveD) / 2])
      sphere( d = CurveD, $fn = 50);
    translate([(XDim - CurveD)/ 2, (YDim  - CurveD)/ 2, -(ZDim - CurveD) / 2])
      sphere( d = CurveD, $fn = 50);
    translate([-(XDim - CurveD)/ 2, -(YDim  - CurveD)/ 2, (ZDim - CurveD) / 2])
      sphere( d = CurveD, $fn = 50);
    translate([(XDim - CurveD)/ 2, -(YDim  - CurveD)/ 2, (ZDim - CurveD) / 2])
      sphere( d = CurveD, $fn = 50);
    translate([-(XDim - CurveD)/ 2, (YDim  - CurveD)/ 2, (ZDim - CurveD) / 2])
      sphere( d = CurveD, $fn = 50);
    translate([(XDim - CurveD)/ 2, (YDim  - CurveD)/ 2, (ZDim - CurveD) / 2])
      sphere( d = CurveD, $fn = 50);
  }
}


module Fibonacci()
{
  phi = 1.6;

  function radius(angle) = 13 * pow(phi, (angle / 90));

  scale(1/25.4)
    for(i = [0:3:270])
      hull()
      {
        translate([sin(i) * radius(i), cos(i) * radius(i)]) circle(3);
        circle(3);
      }
}

module LinearFib(T, Offset, Qual)
{
  Inner = .65;
  Outer = 2.4;
  Start = 0;
  Stop = 210;
  Step = Qual;
  D = 0.24;
  
  Spacing = (Outer - Inner) / (Stop - Start);
  Rotation = 1;
  for(i = [Start:Step:Stop]) 
  {
    hull()
    {
      rotate(-(i * Rotation) + Offset, [0, 0, 1])
        translate([(i * Spacing) + Inner, 0, 0])
          rotate(22 - (i  * .052 * (Qual / 10)), [0, 0, 1])
            cube([.001, .04 + (Qual * 0.025) + (i * 0.0003), T], center = true);
      cylinder(d = .001, h = T, center = true);
    }
  }
}

module ShooterCamAndServo(Rot)
{
  rotate(90, [1, 0, 0])
    rotate(90, [0, 1, 0])
      translate([0, 0, .53])
        ShooterCam(Qual = 5, Rot = Rot);
    rotate(90, [0, 1, 0])
      Servo(DoHorn = 1);
}

module ShooterCam(Qual, Rot)
{
  color("steelblue")
  rotate(Rot, [0, 0, 1])
    difference()
    {
      union()
      {
        mirror([0, 0, 0])
        LinearFib(T = .2, Offset = 0, Qual = Qual);
        cylinder(d = 1.28, h = 0.2, center = true);
      }
      cylinder(d = 10 / 25.4, h = .5, center = true);//Center hole
      translate([(16 / 25.4) / 1.41, 0, 0])
        cylinder(d = 4 / 25.4, h = .5, center = true);
      translate([-(16 / 25.4) / 1.41, 0, 0])
        cylinder(d = 4 / 25.4, h = .5, center = true);
      translate([0, (16 / 25.4) / 1.41, 0])
        cylinder(d = 4 / 25.4, h = .5, center = true);
      translate([0, -(16 / 25.4) / 1.41, 0])
        cylinder(d = 4 / 25.4, h = .5, center = true);
    }
}

module Servo(DoHorn)
{
  scale(1/25.4)
  {
    translate([ServoRotateOffset, 0, 0])
      import("components/GoBildaServoLoRes.stl");
    if (DoHorn == 1)
      translate([0, 0, 2.85])
        rotate(90, [1, 0, 0])
          import("components/GoBilda 1908-0025-0032 ServoHub-low.stl");
  }
}

module RampPost()
{
  translate([.4, 0, 0])
  difference()
  {
    union()
    {
      cylinder(d = .7, h = RampWidth, center = true);
      translate([-0.25, 0, 0])
        cube([.5, .7, RampWidth], center = true);
    }
    cylinder(d = MountD, h = RampWidth + .01, center = true, $fn = MountShape);
  }
}

module RampPost2(Stub = true)
{
  translate([.13, 0, 0])
  difference()
  {
    union()
    {
      cylinder(d = .5, h = RampWidth, center = true);
      if (Stub)
        translate([-0.1, 0, 0])
          cube([.2, .5, RampWidth], center = true);
    }
    cylinder(d = MountD, h = RampWidth + .01, center = true, $fn = MountShape);
  }
}

module RampPostPlug()
{
  difference()
  {
    cylinder(d = MountD, h = 1, $fn = MountShape, center = true);
    cylinder(d1 = 3.95 / 25.4, d2 = 3.85 / 25.4, h = 1.01, center = true);
  }
}

module ShooterCurve(StartA, EndA)
{
  translate([-(OuterD / 2) - .13, 0, 0])
  {
    //Ramp inner curve
    CurveShell(InnerD, OuterD, Width, StartA, EndA);
    PostCount = ceil((EndA - StartA - 4.5) / ((90 + 16) / 5)) - 1;
    for (i = [0:PostCount])
    {
      Angle = ((90 + 16) * i / 4.421);
      echo ("Hole ", i, " angle = ", Angle);
      rotate(Angle, [0, 0, 1])
        translate([(InnerD / 2) + (RampT / 2), 0, 0])
          RampPost2(Stub = i);
    }
  }
}

module CurveShell(InnerD, OuterD, Width, StartA = 0, EndA = 115)
{
  rotate(StartA, [0, 0, 1])
    rotate_extrude(angle = EndA - StartA, convexity = 2)
      translate([InnerD / 2, -Width / 2, 0])
        square([(OuterD - InnerD) / 2, Width]);

}

module Drum()
{  
  color("darkgray")
  rotate(90, [0, 1, 0])
  {
    //Shooter drum
    cylinder(d = 96 / 25.4, h = 5.7, center = true);
    cylinder(d = .5, h = 7, center = true);
  }
}

module ShooterLeadin()
{
  rotate(90, [0, 1, 0])
    translate([(-.5 + .13) / 2, - 6 / 2 , 0])
    {
      cube([RampT / 2, 5.5, RampWidth], center = true);
      translate([(RampT / 2) - 0.062, 0, 0])
      {
        translate([0, - 2.5, 0])
          RampPost2();
        RampPost2();
        translate([0, 2.5, 0])
          RampPost2();
      }
    }
}

module FlapperSpike(Length, BaseD)
{  
  scale([1.0, 1.0, 1.0])
    rotate(-90, [1, 0, 0])
    {
      cylinder(d = BaseD, h = Length - (5/25.4));
      translate([0, 0, Length - (5/25.4)])
        cylinder(d2 = BaseD - (2/25.4), d1 = BaseD + (4/25.4), h = (5/25.4));
    }
}


module ShaftCore(Hub, Thickness)
{
  difference()
  {
    cylinder(d = Hub, h = Thickness, center = true);
    cylinder(d = 8.15 / 25.4, h = Thickness + .1, center = true, $fn = 6);
    rotate(90, [0, 1, 0])
      cylinder(d = 2.9 / 25.4, h = Hub);
  }
}

module Flapper(Size)
{
  Thickness = 0.394;
  Hub = 0.709;

  difference()
  {
    union()
    {
      ShaftCore(Hub, Thickness);
      for (i = [0:5])
      {
        rotate((360/6) * i, [0, 0, 1])
          translate([0, (Hub / 2) - (1 / 25.4), 0])
            if (Size == 0)
              FlapperSpike(Length = (12/25.4), BaseD = (6/25.4));//Small
            else
              FlapperSpike(Length = (12/25.4), BaseD = (8/25.4));//Large
      }
    }
  scale(1/25.4)
    rotate(90, [0, 1, 0])
      cylinder(d = 2.9, h = Hub);
  }
}

module CurveSidePanel()
{
  translate([0, 0, (OuterD / 2) + .13])
    rotate(90, [0, 1, 0])
    {
      intersection()
      {
        cylinder(d = OuterD + .76, h = PanelT);
        rotate(-12, [0, 0, 1])
          translate([0, .76, -0.5])
            cube([(OuterD / 2) + 1, (OuterD / 2) + 1, 1]);
      }
      hull()
      {
        cylinder(d = 3, h = PanelT);
        translate([.8, 3.515, 0])
          cylinder(d = 3, h = PanelT);
        translate([0, -3.515, 0])
          cylinder(d = 3, h = PanelT);
      }
    }
}

module CurveSidePanels()
{
  translate([RampWidth / 2, 0, 0])
    CurveSidePanel();
  mirror([1, 0, 0])
    translate([RampWidth / 2, 0, 0])
      CurveSidePanel();
}

module CamBearing()
{
  color("silver")
  rotate(90, [0, 1, 0])
    cylinder(d = .5, h = .3);
}

module ShooterFlywheelAssembly()
{
  rotate(ShooterAngle, [1, 0, 0])
  {
    rotate(90, [0, 1, 0])
      ShooterCurve(StartA = 0, EndA = 75);//Original test ramp. Used for back spin orientation
    translate([0, 0, (OuterD / 2) + 0.13])
      Drum();
    CurveSidePanels();
    translate([(RampWidth / 2) + PanelT, 3.8, 3.7])
      CamBearing();
    mirror([1, 0, 0])
      translate([(RampWidth / 2) + PanelT, 3.8, 3.7])
        CamBearing();
  }
}

module FullAssembly()
{
  ShooterFlywheelAssembly();
  ShooterLeadin();
  RotAngle = 60 + (ShooterAngle + 12) * (260 / 24);
  translate([-3.8, 4, 2])
    ShooterCamAndServo(Rot = RotAngle);
  mirror([1, 0, 0])
  translate([-3.8, 4, 2])
    ShooterCamAndServo(Rot = RotAngle);
}


module Donut(T, D)
{
  rotate_extrude(angle = 360)
    translate([D, 0, 0]) // Distance from the center of the ring (major radius)
      circle(d = T);    // Radius of the donut tube (minor radius)}
}

module Donut2(T, D1, D2)
{
  rotate_extrude(angle = 360)
    hull()
    {
      translate([D1 / 2, 0, 0]) // Distance from the center of the ring (major radius)
        circle(d = T);    // Radius of the donut tube (minor radius)}
      translate([D2 / 2, 0, 0]) // Distance from the center of the ring (major radius)
        circle(d = T);    // Radius of the donut tube (minor radius)}
    }
}

module RingPulley()
{
  Thickness = 0.394;
  Hub = 1.378;
  
  difference()
  {
    ShaftCore(Hub, Thickness);
    Donut2(D1 = Hub - 0.2, D2 = Hub + .1, T = 0.2);
  }
}

module CapHeadHole()
{
  rotate(-90, [1, 0, 0])
  {
    cylinder(d = .29, h = .17);
    cylinder(d = .17, h = .5);
  }
}

module CapHeadHoleRow(WallT)
{
  translate([8/25.4, -WallT / 2, 0])
    CapHeadHole();
  translate([16/25.4, -WallT / 2, 0])
    CapHeadHole();
  translate([-8/25.4, -WallT / 2, 0])
    CapHeadHole();
  translate([-16/25.4, -WallT / 2, 0])
    CapHeadHole();
}

module VerticalBatterHolder()
{
  X = 1.82;
  Y = 1.87;
  Z = 2.5;
  WallT = 0.26;
  Wall2T = WallT + WallT;
  
  difference()
  {
    RoundedBlock(XDim = X + Wall2T, YDim = Y + Wall2T, ZDim = Z + Wall2T, CurveD = .2);
    translate([0, 0, WallT])
      RoundedBlock(XDim = X, YDim = Y, ZDim = Z + Wall2T, CurveD = .05);
    translate([0, 0, -Z /2])
      cylinder(d = 1.5, h = 1, center = true);
    rotate(90, [1, 0, 0])
      cylinder(d = 1.5, h = 3);
    rotate(90, [0, 1, 0])
      cylinder(d = 1.5, h = 3, center = true);
      translate([0, (X + WallT + .049) / 2, (Z + Wall2T + 2 - 0.4) / 2 - (64 / 25.4)])
        CapHeadHoleRow(WallT = WallT);
      translate([0, (X + WallT + .049) / 2, (Z + Wall2T + 2 - 0.4) / 2 - (56 / 25.4)])
        CapHeadHoleRow(WallT = WallT);
      translate([0, (X + WallT + .049) / 2, (Z + Wall2T + 2 - 0.4) / 2 - (48 / 25.4)])
        CapHeadHoleRow(WallT = WallT);
      translate([0, (X + WallT + .049) / 2, (Z + Wall2T + 2 - 0.4) / 2 - (72 / 25.4)])
        CapHeadHoleRow(WallT = WallT);
  }
  translate([0, (Y + WallT) / 2, (Z + Wall2T + 2 - 0.4) / 2])
    difference()
    {
      RoundedBlock(XDim = X + Wall2T, YDim = WallT, ZDim = 2, CurveD = .2);
      CapHeadHoleRow(WallT = WallT);
      translate([0, 0, 8 / 25.4])
        CapHeadHoleRow(WallT = WallT);
      translate([0, 0, -16 / 25.4])
        CapHeadHoleRow(WallT = WallT);
    }  
}



////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////

//rotate(90, [0, 1, 0])
//  ShooterCurve(StartA = 0, EndA = 115);//Original test ramp. Used for back spin orientation
//  ShooterCurve(StartA = 0, EndA = 75);//Original test ramp. Used for back spin orientation

//translate([0, ((4 + PollenD + PollenD - Compression + RampT) / 2) + .4, 0])
//  cylinder(d = .5, h = RampWidth, center = true);
//echo ("Mount radius = ", ((4 + PollenD + PollenD - Compression + RampT) / 2) + .4);

//RampPostPlug();

//Test ramp spacers
//Spacer1
//CurveShell(InnerD - .2, InnerD, Width);
//Spacer2
//CurveShell(InnerD - .2 - 0.2, InnerD - 0.2, Width);
//Spacer3
//CurveShell(InnerD - .2 - 0.2 - 0.2, InnerD - 0.2 - 0.2, Width);
Flapper(Size = 0);

//RingPulley();

//ShooterLeadin();
//ShooterCam(Qual = .5, Rot = Rot);
//VerticalBatterHolder();

//FullAssembly();
