$fn         = 100;
RampWidth   = 3.8;
PollenD     = 2.8;
Compression = 0.25;
RampT       = 0.25;
MountD      = 3.95 / 25.4;//0.5824;//0.5824 for 0.5 hex shaft
MountShape  = 50;//6 for 0.5 hex shaft

InnerD = 4 + PollenD + PollenD - Compression;
OuterD = 4 + PollenD + PollenD - Compression + RampT;
Width  = RampWidth;

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

module RampPostPlug()
{
  difference()
  {
    cylinder(d = MountD, h = 1, $fn = MountShape, center = true);
    cylinder(d1 = 3.95 / 25.4, d2 = 3.85 / 25.4, h = 1.01, center = true);
  }
}

module ShooterCurve()
{
  //Ramp inner curve
  CurveShell(InnerD, OuterD, Width);
  for (i = [0:5])
  {
    Angle = ((90 + 16) * i / 5) + 4.5;
    echo ("Hole ", i, " angle = ", Angle);
    rotate(Angle, [0, 0, 1])
      translate([(InnerD / 2) + (RampT / 2), 0, 0])
        RampPost();
  }
}

module CurveShell(InnerD, OuterD, Width)
{
  intersection()
  {
    difference()
    {
      cylinder(d = OuterD, h = Width, center = true);
      cylinder(d = InnerD, h = Width + 0.01, center = true);
    }
    translate([-2, 0, -5])
      cube([10, 10, 10]);
  }
}

module Drum()
{  
  rotate(90, [0, 1, 0])
  {
    //Shooter drum
    cylinder(d = 4, h = 5, center = true);
  }
}

module ShooterLeadin()
{
  translate([(InnerD + (RampT / 2)) / 2, - 6 / 2 , 0])
  {
    cube([RampT / 2, 6, RampWidth], center = true);
    translate([(RampT / 2), 0, 0])
    {
      translate([0, - 2.5, 0])
        RampPost();
      RampPost();
      translate([0, 2.5, 0])
        RampPost();
    }
  }
}

module FlapperSpike(Length, BaseD)
{  
  scale([1.0, 1.0, 1.0])
    rotate(-90, [1, 0, 0])
    {
      cylinder(d = BaseD, h = Length - 5);
      translate([0, 0, Length - 5])
        cylinder(d2 = BaseD - 2, d1 = BaseD + 4, h = 5);
    }
}

module Flapper()
{
  Thickness = 10;
  Hub = 18;
  
  difference()
  {
    cylinder(d = Hub, h = Thickness, center = true);
    cylinder(d = 8.05, h = Thickness + .1, center = true, $fn = 6);
  }
  for (i = [0:5])
  {
    rotate((360/6) * i, [0, 0, 1])
      translate([0, (Hub / 2) - 1, 0])
//        FlapperSpike(Length = 12, BaseD = 6;);//Small
        FlapperSpike(Length = 12, BaseD = 8);//Large
  }
}

//rotate(90, [0, 1, 0])
//  ShooterCurve();
ShooterLeadin();
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

Flapper();
