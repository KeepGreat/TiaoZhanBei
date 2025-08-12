import requests

url = 'http://localhost:5000/predict'
files = {'file': open('D:/code/tzb/model/3.jpg', 'rb')}
response = requests.post(url, files=files)

print(response.json())
# 预期输出: 
# {'置信度': 0.5227611660957336, '预测类型': 'Stable'}