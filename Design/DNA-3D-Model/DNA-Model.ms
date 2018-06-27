set Animate off

-- Creating the Axis

c= box()

c.width=500

c.height=15

c.length=15

c.pivot=c.center

c.pos=[0,0,0]

axis_material= meditmaterials[2]

c.mat=axis_material

p=convertTopoly(c)

-- creating the End cubes of the axis

end= box()

end.gbufferchannel = 1

end.height=80

end.length=80

end.width=80

end.pivot=end.center

end.pos=[250,0,0]

end.lengthsegs=10

end.widthsegs=10

end.heightsegs=10

ms=meshsmooth()

ms.iterations=2

addmodifier end ms

glass_end= meditmaterials[7]

end.mat= glass_end

end2= instance end pos:[-250,0,0]

end2.gbufferchannel = 1

-- creating the tiny nodes on the axis

n1=box()

p=convertTopoly(n1)

n1.pivot=n1.center

n1.pos=[0,0,0]

n1.pos= [136.885,0,0]

rotate n1 (angleaxis -48.7714 [1,0,0])

n2=box()

p=convertTopoly(n2)

n2.pivot=n2.center

n2.pos=[0,0,0]

 n2.pos= [94.5886,0,0]

rotate n2 (angleaxis -20.6298 [-1,1,0])

n3= box()

p=convertTopoly(n3)

n3.pivot=n3.center

n3.pos=[0,0,0]

 n3.pos= [-162.011,0,0]

rotate n3 (angleaxis -27.2908 [0,0,1])

n4= box()

p=convertTopoly(n4)

n4.pivot=n4.center

n4.pos=[0,0,0]

n4.pos= [-124.776,0,0]

rotate n4 (angleaxis -73.3431 [-1,-1,0])

--Attachment

polyOp.attach n4 n3

polyOp.attach n4 n2

polyOp.attach n4 n1

polyOp.attach c n4

-- modifying axis rotation

c.pivot= [0,0,0]

end.pivot= [0,0,0]

end2.pivot= [0,0,0]

/*n1.pivot= [0,0,0]

n2.pivot= [0,0,0]

n3.pivot= [0,0,0]

n4.pivot= [0,0,0]*/

-- Instances and Rotation

set Animate on

for count= 1 to 12 do

(

sliderTime=0

ci = instance c

ci.pos.y= count*200

rotate ci (angleaxis (30*count) [0,1,0])

sliderTime=100

ci.pos.y = (count+1)*200

rotate ci (angleaxis (30*(count+1)) [0,1,0])

sliderTime=0

endi = instance end

rotate endi (angleaxis (30*count) [0,1,0])

endi.pos.y= count*200

endi.gbufferchannel = 1

sliderTime=100

endi.pos.y = (count+1)*200

rotate endi (angleaxis (30*(count+1)) [0,1,0])

sliderTime=0

end2i=instance end2

end2i.pos.y= count*200

rotate end2i (angleaxis (30*count) [0,1,0])

end2i.gbufferchannel = 1

sliderTime=100

end2i.pos.y = (count+1)*200

rotate end2i (angleaxis (30*(count+1)) [0,1,0])

)

/*sliderTime=0

n1i=instance n1

n1i.pos.y= count*200

rotate n1i (angleaxis (30*count) [0,1,0])

sliderTime=100

n1i.pos.y = (count+1)*200

rotate n1i (angleaxis (30*(count+1)) [0,1,0])

sliderTime=0

n2i=instance n2

n2i.pos.y= count*200

rotate n2i (angleaxis (30*count) [0,1,0])

sliderTime=100

n2i.pos.y = (count+1)*200

rotate n2i (angleaxis (30*(count+1)) [0,1,0])

sliderTime=0

n3i=instance n3

n3i.pos.y= count*200

rotate n3i (angleaxis (30*count) [0,1,0])

sliderTime=100

n3i.pos.y = (count+1)*200

rotate n3i (angleaxis (30*(count+1)) [0,1,0])

sliderTime=0

n4i=instance n4

n4i.pos.y= count*200

rotate n4i (angleaxis (30*count) [0,1,0])

sliderTime=100

n4i.pos.y = (count+1)*200

rotate n4i (angleaxis (30*(count+1)) [0,1,0])*/

node_material=meditmaterials[8]

ms=meshsmooth()

function draw_right xr zr =

(

set Animate off

sliderTime=0

for i= 0 to 60

do (

b=box()

b.mat=node_material

b.height=40

b.length=40

b.width=40

b.heightsegs=10

b.lengthsegs=10

b.widthsegs=10

b.pivot=b.center

--b.pos.z= zr* (sin (i*12))

--b.pos.y= i *80

--b.pos.x =  -xr* (cos (i*12))

b.pos=[0,0,0]

b.pos.x= xr

b.pos.z=zr

b.pivot=[0,0,0]

rotate b (angleaxis (i*6) [0,1,0])

b.pos.y= i*40

addmodifier b ms

b.pivot=b.center

rotate b (angleaxis (random 0 360) [(-1^i),(-1^i),(-1^i)])

)

)

function draw_left xr zr =

(

for i= 0 to 60

do (

b=box()

b.mat=node_material

b.height=40

b.length=40

b.width=40

b.heightsegs=10

b.lengthsegs=10

b.widthsegs=10

b.pivot=b.center

--b.pos.z= zr* (sin (i*12))

--b.pos.y= i *80

--b.pos.x =  -xr* (cos (i*12))

b.pos=[0,0,0]

b.pos.x= -xr

b.pos.z=zr

b.pivot=[0,0,0]

rotate b (angleaxis (i*6) [0,1,0])

b.pos.y= i*40

addmodifier b ms

b.pivot=b.center

rotate b (angleaxis (random 0 360) [(-1^i),(-1^i),(-1^i)])

)

)

draw_right 300 0

draw_right 300 50

draw_right 350 0

draw_right 350 50

draw_left300 0

draw_left 300 50

draw_left 350 0

draw_left 350 50