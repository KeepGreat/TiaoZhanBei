<template>
    <div class="image-recognition-container">
        <h2>图像识别</h2>
        <!-- 输入病人信息 -->
        <el-form :model="form" label-width="120px">
            <el-form-item label="病人姓名">
                <el-input v-model="form.patientName" placeholder="请输入病人姓名"></el-input>
                </el-form-item>
            <el-form-item label="病人年龄">
                <el-input v-model="form.patientAge" type="number" placeholder="请输入病人年龄"></el-input>
            </el-form-item>
            <el-form-item label="病人性别">
                <el-radio-group v-model="form.patientGender">
                    <el-radio :value="'男'">男</el-radio>  
                    <el-radio :value="'女'">女</el-radio>
                </el-radio-group>
            </el-form-item>
        </el-form>

        <!-- 上传自然光和伍德灯图像 -->
        <div class="upload-section">
            <!-- 自然光图像上传 -->
            <div class="upload-item">
                <el-upload class="upload-demo" action="#"
                    :on-change="(file, fileList) => handleImageChange(file, fileList, 'naturalLight')"
                    :before-upload="beforeUpload" :multiple="false" :limit="1" ref="naturalLightUploadRef" accept="image/*"
                    :auto-upload="false">
                    <template #trigger>
                        <el-button type="primary">上传自然光图像</el-button>
                    </template>
                </el-upload>
                <!-- 自然光图像预览 -->
                <img v-if="naturalLightImageUrl" :src="naturalLightImageUrl" alt="自然光图像"
                    style="max-width: 200px; margin-top: 10px;">
            </div>

            <!-- 伍德灯图像上传 -->
            <div class="upload-item">
                <el-upload class="upload-demo" action="#"
                    :on-change="(file, fileList) => handleImageChange(file, fileList, 'woodLamp')"
                    :before-upload="beforeUpload" :multiple="false" :limit="1" ref="woodLampUploadRef" accept="image/*"
                    :auto-upload="false">
                    <template #trigger>
                        <el-button type="primary">上传伍德灯图像</el-button>
                    </template>
                </el-upload>
                <!-- 伍德灯图像预览 -->
                <img v-if="woodLampImageUrl" :src="woodLampImageUrl" alt="伍德灯图像"
                    style="max-width: 200px; margin-top: 10px;">
            </div>
        </div>

        <!-- 四个操作按钮 -->
        <div class="button-group">
            <el-button type="primary" @click="handleUploadImage">上传图像</el-button>
            <el-button type="warning" @click="handleRecognition">识别图像</el-button>
            <el-button type="success" @click="handleUploadResult">上传结果</el-button>
            <el-button type="info" @click="handleClearData">清空数据</el-button>
        </div>

        <!-- 显示识别结果 -->
        <div v-if="recognitionResult" class="result-section">
            <h3>识别结果</h3>
            <p>{{ recognitionResult }}</p>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { ElMessage, ElLoading } from 'element-plus';

// 表单数据
const form = ref({
    patientAge: null,
    patientGender: '男',
    naturalLightImage: null,
    woodLampImage: null
});

// 上传组件引用
const naturalLightUploadRef = ref(null);
const woodLampUploadRef = ref(null);

// 识别结果
const recognitionResult = ref('');

// 图像预览 URL
const naturalLightImageUrl = ref('');
const woodLampImageUrl = ref('');

// 处理图像上传变化
const handleImageChange = (file, fileList, type) => {
    if (type === 'naturalLight') {
        form.value.naturalLightImage = file.raw;
        // 创建图像预览 URL
        const reader = new FileReader();
        reader.onload = (e) => {
            naturalLightImageUrl.value = e.target.result;
        };
        reader.readAsDataURL(file.raw);
    } else if (type === 'woodLamp') {
        form.value.woodLampImage = file.raw;
        // 创建图像预览 URL
        const reader = new FileReader();
        reader.onload = (e) => {
            woodLampImageUrl.value = e.target.result;
        };
        reader.readAsDataURL(file.raw);
    }
};

// 上传前检查
const beforeUpload = (file) => {
    const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJPG) {
        ElMessage.error('只能上传 JPG/PNG 格式的图片');
    }
    // 限制文件大小（可选）
    const isLt5M = file.size / 1024 / 1024 < 5;
    if (!isLt5M) {
        ElMessage.error('上传图片大小不能超过 5MB!');
    }
    return isJPG && isLt5M;
};

// 处理上传图像
const handleUploadImage = () => {
    if (!form.value.naturalLightImage || !form.value.woodLampImage) {
        ElMessage.error('请上传自然光和伍德灯图像');
        return;
    }
    ElMessage.success('图像上传成功');
};

// 处理图像识别
const handleRecognition = async () => {
    if (!form.value.patientAge || !form.value.patientGender || !form.value.naturalLightImage || !form.value.woodLampImage) {
        ElMessage.error('请填写完整信息并上传两张图像');
        return;
    }

    // 显示加载状态
    const loading = ElLoading.service({
        lock: true,
        text: '识别中，请稍候...',
        background: 'rgba(0, 0, 0, 0.7)'
    });

    try {
        const formData = new FormData();
        formData.append('file', form.value.woodLampImage);

        const response = await axios.post('http://localhost:5000/predict', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
        });

        // 返回结果示例: { '置信度': 0.5227611660957336, '预测类型': 'Stable' }
        const data = response.data;
        const diagnosisText = `${data['预测类型']} ${(data['置信度'] * 100).toFixed(2)}%`;

        
        // 2. 提取图片的 Base64 数据和类型（从预览 URL 中）
        const image1Base64 = naturalLightImageUrl.value.split(',')[1];
        const image1Type = form.value.naturalLightImage.type;
        const image2Base64 = woodLampImageUrl.value.split(',')[1];
        const image2Type = form.value.woodLampImage.type;
        
        // 3. 完整保存结果到 form.value.recognitionResult
        form.value.recognitionResult = {
            diagnosis: diagnosisText,
            image1: image1Base64,
            image1Type: image1Type,
            image2: image2Base64,
            image2Type: image2Type,
            createdAt: new Date().toISOString().slice(0, 19) // 符合格式的时间
        };
        
        // 4. 同时更新页面显示用的 recognitionResult
        const showDiagnosisText = `预测结果：${data['预测类型']} 置信度：${(data['置信度'] * 100).toFixed(2)}%`;
        recognitionResult.value = showDiagnosisText;
        
        ElMessage.success('识别成功');
    } catch (error) {
        console.error('图像识别失败:', error);
        ElMessage.error('图像识别失败，请重试');
    } finally {
        loading.close();
    }
};

// 处理上传结果
const handleUploadResult = async () => {
    if (!recognitionResult.value) {
        ElMessage.error('请先进行图像识别');
        return;
    }

    try {
        const requestData = {
            // 病人基本信息
            patientName: form.value.patientName,
            patientAge: form.value.patientAge,
            patientGender: form.value.patientGender,
            keyword: form.value.keyword || '皮肤检查',
            
            // 识别结果信息
            diagnosis: form.value.recognitionResult.diagnosis,
            
            // 图像信息（Base64格式）
            image1: form.value.recognitionResult.image1,
            image1Type: form.value.recognitionResult.image1Type,
            image2: form.value.recognitionResult.image2,
            image2Type: form.value.recognitionResult.image2Type,
            
            // 创建时间
            createdAt: form.value.recognitionResult.createdAt
        };
        
        const response = await axios.post('http://localhost:8080/info', requestData);
        if (response.data === '保存成功') {
            ElMessage.success('结果保存成功');
        } else {
            ElMessage.error(response.data);
        }
    } catch (error) {
        console.error('保存结果失败:', error);
        ElMessage.error('保存结果失败，请重试');
    }
};

// 处理清空数据
const handleClearData = () => {
    form.value.patientAge = null;
    form.value.patientGender = '男';
    form.value.naturalLightImage = null;
    form.value.woodLampImage = null;
    recognitionResult.value = '';
    // 清空上传组件
    naturalLightUploadRef.value?.clearFiles();
    woodLampUploadRef.value?.clearFiles();
    // 清空图像预览 URL
    naturalLightImageUrl.value = '';
    woodLampImageUrl.value = '';
    ElMessage.success('数据已清空');
};
</script>

<style scoped>
.image-recognition-container {
    padding: 20px;
    max-width: 1000px;
    margin: 0 auto;
}

.upload-section {
    display: flex;
    gap: 40px;
    margin-bottom: 30px;
    align-items: flex-start;
}

.upload-item {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.button-group {
    display: flex;
    gap: 20px;
    margin-bottom: 30px;
    flex-wrap: wrap;
}

.result-section {
    border: 1px solid #ccc;
    padding: 20px;
    border-radius: 4px;
    margin-top: 20px;
}

h2 {
    color: #333;
    margin-bottom: 20px;
}

h3 {
    color: #666;
    margin-bottom: 10px;
}
</style>