# Solar System using JavaFx | Springboot (in development)
## Intro
I was always been intrigued to figure out how each planet orbiting our sun and how fascinating their are. Also, I need to make a step further about the Java programming language.  



## Getting start
Clone the project

## Rotation of an ellipse
### Rotation centered at the origin (0,0)
```math
x = rx cos(t)
y = ry sin(t)

where:
rx: radius along the x-axis
ry: radius along the y-axis
t: rotation's degree

*in case of circle rx = ry
```
Rotation formulae
```math
x = rx cos(t) + h sin(t)
y = ry sin(t) + h cos(t)

where:
h: inclination degree
```
### Rotation not centered at the origin
```math
x = h + rx cos(t)
y = k + ry sin(t)

where:
rx: radius along the x-axis
ry: radius along the y-axis
t: rotation's degree
h and k are coordinates of the point to rotate around

*in case of circle r = rx = ry
```
Rotation formulae
```math
x = h + rx cos(t) + l sin(t)
y = k + ry sin(t) + l cos(t)

where:
l: inclination degree
```

## Planetary's position
Here is a [documentation](http://www.stjarnhimlen.se/comp/tutorial.html) about how to compute a planetary's position at any given date. 
Also, check [this](http://www.stjarnhimlen.se/comp/ppcomp.html) out

## Screenshot
Camera view is at 90 degree on the y axis