m=[6    69  5	8	53	10	82	14	73	15	66	52	98	65	81	46	44	83	9	82];
processors=4;

tasks=sort(m,'descend');
queue = zeros(1,processors);
index = ones (processors);

for i=1:size(tasks,2)
%for i=1:9
    disp ('Current Task');
    disp (tasks(i));
    for j=1:processors
        capacity (j)=sum(queue(:,j));
        [val,loc]= min(capacity);
    end
        disp ('capacity');
    disp (capacity);
    disp ('Current Queue');
    disp (queue);
    disp ('Least busy processor');
    disp (loc);
    queue (index(loc),loc)=tasks(i);
    index(loc)=index(loc)+1;
    disp ('New Queue');
    disp (queue);
end