from flask import Flask, request, jsonify
import os
from predict import predict

app = Flask(__name__)
CORS(app)
UPLOAD_FOLDER = os.path.dirname(os.path.abspath(__file__))
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

# 配置参数
SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
MODEL_PATH = os.path.join(SCRIPT_DIR, 'best_model_accuracy_adapter.pth')
CLASS_NAMES = ['Developing', 'Stable']

@app.route('/predict', methods=['POST'])
def predict_api():
    try:
        print("收到预测请求")
        if 'file' not in request.files:
            print("未提供文件")
            return jsonify({"error": "No file provided"}), 400
        
        file = request.files['file']
        if file.filename == '':
            print("空文件名")
            return jsonify({"error": "Empty filename"}), 400

        file_path = os.path.join(app.config['UPLOAD_FOLDER'], file.filename)
        print(f"尝试保存文件到: {file_path}")
        
        file.save(file_path)
        print(f"文件保存成功: {os.path.exists(file_path)}")

        print(f"加载模型: {MODEL_PATH}")
        print(f"模型存在: {os.path.exists(MODEL_PATH)}")
        
        result = predict(MODEL_PATH, file_path, CLASS_NAMES)
        print(f"预测结果: {result}")
        
        os.remove(file_path)
        return jsonify(result)
    except Exception as e:
        print(f"预测异常: {str(e)}")
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
