# Solar System using JavaFx (in development)
## Intro
I was always been intrigued to figure out how each planet orbiting our sun and how fascinating their are. Also, I need to make a step further about the Java programming language.  

I know how to write a conditionnal statement, a loop, a simple program, a web site application like anyone else (programers) but I always wondered how they build games with stunning graphic? How they build a desktop application with such an amazing UI?. How they do it? Then, I found the existance of the JavaFx few days after building my first java desktop application using JFrame. And once again, I just started. I told myself to pick a project/theme up to begin with no matter how difficult it will be.  


This project has been a tough one for me but finally it was enriching.

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
Rotation formula
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
Rotation formula
```math
x = h + rx cos(t) + l sin(t)
y = k + ry sin(t) + l cos(t)

where:
l: inclination degree
```

## Screenshot
Camera view is at 90 degree on the y axis
