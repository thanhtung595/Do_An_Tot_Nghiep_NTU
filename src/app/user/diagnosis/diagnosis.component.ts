import {
  Component,
  ElementRef,
  OnInit,
  ViewChild,
  AfterViewChecked
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DiagnosisApiService } from '@app/services/api/diagnosis/diagnosis.api.service';
import { API_BASE_URL } from '@app/constants'

@Component({
  selector: 'app-diagnosis',
  templateUrl: './diagnosis.component.html',
  styleUrls: ['./diagnosis.component.css']
})
export class DiagnosisComponent implements OnInit, AfterViewChecked {
  diagnosisForm: FormGroup;
  isLoading: boolean = false;
  // chatMessages: { type: 'user' | 'ai'; message: string }[] = [];

  @ViewChild('chatBox') chatBox!: ElementRef;

  constructor(
    private fb: FormBuilder,
    private diagnosisApiService: DiagnosisApiService
  ) {
    this.diagnosisForm = this.fb.group({
      symptoms: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  getImageUrl(imgPath: string): string {
    return `${API_BASE_URL}${imgPath}`;
  }

  ngOnInit(): void {
    this.restoreChatHistory(); // ✅ Khôi phục khi khởi động
  }

  ngAfterViewChecked(): void {
    this.scrollToBottom();
  }

  scrollToBottom(): void {
    try {
      this.chatBox.nativeElement.scrollTop = this.chatBox.nativeElement.scrollHeight;
    } catch (err) {
      console.error('Scroll Error:', err);
    }
  }

  chatMessages: {
    type: 'user' | 'ai';
    message: string;
    doctors?: any[]; // Thêm dòng này
  }[] = [];

  onSubmit() {
    if (this.diagnosisForm.invalid) return;

    const symptoms = this.diagnosisForm.value.symptoms.trim();
    this.chatMessages.push({ type: 'user', message: symptoms });
    this.saveChatToSession();

    this.isLoading = true;
    this.diagnosisForm.reset();

    this.diagnosisApiService.getDiagnosiAI(symptoms).subscribe({
      next: (data) => {
        const raw = data.diagnosis || 'Không có kết quả.';
        const formatted = raw
          .replace(/\n/g, '<br>')
          .replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;');

        this.chatMessages.push({
          type: 'ai',
          message: formatted,
          doctors: data.suggested_doctors || []
        });

        this.isLoading = false;
        this.saveChatToSession();
      },
      error: (error) => {
        this.chatMessages.push({
          type: 'ai',
          message: 'Đã xảy ra lỗi khi gọi API.'
        });
        this.isLoading = false;
        this.saveChatToSession();
        console.error('API Error:', error);
      }
    });
  }


  // ✅ Hàm lưu xuống sessionStorage
  saveChatToSession() {
    sessionStorage.setItem('chatMessages', JSON.stringify(this.chatMessages));
  }

  // ✅ Hàm khôi phục từ sessionStorage
  restoreChatHistory() {
    const saved = sessionStorage.getItem('chatMessages');
    if (saved) {
      try {
        this.chatMessages = JSON.parse(saved);
      } catch (err) {
        console.error('Lỗi khi parse session chat:', err);
        sessionStorage.removeItem('chatMessages');
      }
    }
  }
}
