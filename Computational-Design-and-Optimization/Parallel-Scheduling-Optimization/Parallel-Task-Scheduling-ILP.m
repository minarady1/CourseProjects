f=zeros(1,60);
f(end+1)=1;

t=[6    69  5	8	53	10	82	14	73	15	66	52	98	65	81	46	44	83	9	82];

A=[];
for i=1:20
   A=[A t(i)*eye(3)];
end
A=[A -1*ones(3,1)];

b=zeros(3,1);
Aeq=[];
j=1;
for i=1:20
   Aeq(i,j)=1;
   j=j+1;
   Aeq(i,j)=1;
   j=j+1;
   Aeq(i,j)=1;
   j=j+1;
end
Aeq(20,61)=0;

beq= ones(20,1);

intcon=1:61;
lb=zeros(61,1);

ub=ones(60,1);
ub(end+1,1)=sum(t);
[x,fval,exitflag,output]  = intlinprog(f,intcon,A,b,Aeq,beq,lb,ub);

