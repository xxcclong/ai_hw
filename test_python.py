a = [[99 for i in range(3)] for i in range(3)]
i = 0
j = 0
while i<3:
    j = 0
    while j<3:
        print(a[i][j])
        print(i)
        print(j)
        j = j + 1
    i = i+1
