f = reshape(data',1,19890);
Aeq=zeros(1989,19890);
beq = ones(1989,1);
x = ones (1,10);
% this loop distributes the ones diagonally for each 10 variables.
for i=0:1988
   Aeq(i+1:i+1, i*10+1:i*10+10)=x;
end
x = zeros(1,10);
x(1,1)=1;
row=repmat(x,1,1989);
for i=0:9
    A(i+1,:) = circshift(row,i);
end
%upper bound per GW
b=ones(10,1)*300;

intcon=1:1:19890;
lb = zeros(1,19890);
ub =ones(1,19890);
[result,fval] = intlinprog(f,intcon,A,b,Aeq,beq,lb,ub);
% c=reshape(f,10,10)';
final=reshape(result,10,1989)';
final_gw = final *[1:1:10]';

for i=1:size(data,1)
    final_links (i,:)= data(i,:)*final(i,:)';
end
histogram(final_gw);
figure;
histogram(final_links);
