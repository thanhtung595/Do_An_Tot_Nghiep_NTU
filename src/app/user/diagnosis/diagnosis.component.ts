import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-diagnosis',
  templateUrl: './diagnosis.component.html',
  styleUrls: ['./diagnosis.component.css']
})
export class DiagnosisComponent {
  diagnosisForm: FormGroup;
  diagnosisResult: string | null = null;
  isLoading: boolean = false;

  constructor(private fb: FormBuilder) {
    this.diagnosisForm = this.fb.group({
      symptoms: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  onSubmit() {
    if (this.diagnosisForm.invalid) {
      return;
    }

    this.isLoading = true;
    this.diagnosisResult = null;

    // Giả lập việc gửi dữ liệu đến AI và nhận kết quả
    setTimeout(() => {
      const symptoms = this.diagnosisForm.value.symptoms;
      this.diagnosisResult = this.simulateAIResponse(symptoms);
      this.isLoading = false;
    }, 2000); // Giả lập thời gian chờ 2 giây
  }

  simulateAIResponse(symptoms: string): string {
    // Giả lập phản hồi từ AI dựa trên triệu chứng
    if (symptoms.toLowerCase().includes('đau đầu')) {
      return 'Có thể bạn đang gặp vấn đề về căng thẳng hoặc đau nửa đầu. Hãy nghỉ ngơi và uống đủ nước.';
    } else if (symptoms.toLowerCase().includes('sốt')) {
      return 'Có thể bạn đang bị nhiễm virus hoặc vi khuẩn. Hãy uống thuốc hạ sốt và đến gặp bác sĩ nếu tình trạng kéo dài.';
    } else {
      return 'Triệu chứng của bạn không rõ ràng. Vui lòng cung cấp thêm thông tin hoặc đến gặp bác sĩ để được kiểm tra chi tiết.';
    }
  }
}
