file2 = open('two_model.json','r')
file1 = open('one_model.json','r')
model1 = json.load(file1)
model2 = json.load(file2)
file1.close()
file2.close()
while(true):

