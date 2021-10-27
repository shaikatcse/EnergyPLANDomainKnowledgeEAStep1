
%This is a ascript to find best individual in terms of diversity using solow-polasky matrix.
%reference: Exploring Structural Diversity in Evolutionary Algorithms.
%Author: Tamara Ulrich

%parameter required sent from java:
%theta: the value od theta
%indvMatrix: matrix that contain the individuls (each row contains an individual, columns are the decision variables)
%numberOfFinalIndv: number of individuals when the script returns

%return: 
%indMatrixTemp: matrix that contains 'numberOfFinalIndv' number of individuals


%theta = 0.00005;

%indvMatrix = rand(numberOfIndv, numberOfDV);
%indvMatrix = 10000 * indvMatrix;


numberOfIndv = size(indvMatrix,1);
indMatrixTemp = indvMatrix;

 

%define elements of Solow and Polasy Matrix
M=zeros(numberOfIndv,numberOfIndv);

%calculate Euclidien distances
for i=1:numberOfIndv
    v1 = indvMatrix(i,:);
    for j=1:numberOfIndv
        v2= indvMatrix(j,:);   
        D  = sqrt(sum((v1 - v2) .^ 2));
        M(i,j)=exp(-theta*D);
    end
end
%define elements of Solow and Polasy Matrix: END

%main calculation: START
sizeM = size(M,1);
for j=sizeM:-1:numberOfFinalIndv+1
   trackRowCol=-1;
   contributionDiv=intmax('uint32');
   MInv = inv(M);
   for i = 1:size(M,1)
      %take a column
       b = MInv(:,i);
       %sum all elements of the column and divided by diogonal element
       % it is because the indivudual we want to investigate should be in the last column and row of the matrix
       %Considering the follwing matrix of distances
       %-1 2 3 
       %2 -2 6
       %3 6 -3
       
       %if we want to calculate the contribution of 1st individual, the
       %distance matrix convert in the following way:
       %-2 6 2
       %6 -3 3
       %2 3 -1
       
       %if we want to calculate the contribution of 2nd individual, the
       %distance matrix convert in the following way:
       %-1 3 2
       %3 -3 6
       %2 6 -2
       
       %At the same time the inverse of the matrix changed similar way.
       %Therefre, the diagonal element alwaeys the last element (c, in the reference text)
       %Therefore, the sum is diveded by the diagonal element, which is i.
       s=sum(b)^2/b(i);
       if  s < contributionDiv
           contributionDiv = s;
           %the row and column number is saved
           trackRowCol = i;
       end
       
       
   end
    %Delete the row and column from M.
    M(trackRowCol,:)=[];
    M(:,trackRowCol)=[];
    %Delete the row from indMatrixTemp
    indMatrixTemp(trackRowCol,:)=[];
    disp(trackRowCol)
    %disp(j)
end
%main calculation: END

