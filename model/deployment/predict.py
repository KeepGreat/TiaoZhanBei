import torch
from torchvision import models, transforms
from PIL import Image
import os
import torch.nn as nn

# ===== Adapter 模块（与训练脚本保持一致）=====
class Adapter(nn.Module):
    def __init__(self, in_features, bottleneck_dim=64):
        super().__init__()
        self.adapter = nn.Sequential(
            nn.Linear(in_features, bottleneck_dim),
            nn.ReLU(inplace=True),
            nn.Linear(bottleneck_dim, in_features)
        )

    def forward(self, x):
        return x + self.adapter(x)

def build_model(num_classes=2, adapter_bottleneck_dim=64):
    model = models.densenet121(weights=None)
    num_ftrs = model.classifier.in_features
    model.classifier = nn.Sequential(
        Adapter(num_ftrs, bottleneck_dim=adapter_bottleneck_dim),
        nn.Dropout(p=0.5, inplace=True),
        nn.Linear(num_ftrs, num_classes)
    )
    return model

# ===== 预测函数 =====
def predict(model_path, image_path, class_names):
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model = build_model(num_classes=len(class_names), adapter_bottleneck_dim=64)
    model.load_state_dict(torch.load(model_path, map_location=device))
    model.to(device)
    model.eval()

    transform = transforms.Compose([
        transforms.Resize(256),
        transforms.CenterCrop(224),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
    ])

    image = Image.open(image_path).convert('RGB')
    input_tensor = transform(image).unsqueeze(0).to(device)

    with torch.no_grad():
        output = model(input_tensor)
        probs = torch.nn.functional.softmax(output, dim=1)[0]
        pred_idx = probs.argmax().item()

    return {
        "预测类型": class_names[pred_idx],
        "置信度": probs[pred_idx].item()
    }
    # print(f"预测类别: {class_names[pred_idx]}, 置信度: {probs[pred_idx].item():.2%}")

# ===== 使用示例 =====
# if __name__ == "__main__":
#     MODEL_PATH = "best_model_accuracy_adapter.pth"  # 训练时保存的文件名
#     IMAGE_PATH = "D:/下载/数据集/白癜风患者统计/旦增欧珠/2024-12-09 17,01,33/DSC00449.JPG"  # 测试图片
#     CLASS_NAMES = ['Developing','Stable']  # 必须和训练时顺序一致
#     predict(MODEL_PATH, IMAGE_PATH, CLASS_NAMES)
