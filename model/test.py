import requests

url = 'http://localhost:5000/predict'
files = {'file': open('3.jpg', 'rb')}
response = requests.post(url, files=files)

print(response.json())
# 预期输出: 
# {
#   "image": "test.jpg",
#   "predicted_class": "Developing",
#   "confidence": "95.23%"
# }