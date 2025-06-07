import { Component, OnInit } from '@angular/core';
import { trigger, transition, style, animate } from '@angular/animations';
import { ConfirmDialogService } from '@app/services/dialog/confirm-dialog.service';
import { ConfirmDialogData } from '@app/model/confirm-dialog.model';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss'],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('200ms ease-out', style({ opacity: 1 }))
      ]),
      transition(':leave', [
        animate('200ms ease-in', style({ opacity: 0 }))
      ])
    ]),
    trigger('slideInOut', [
      transition(':enter', [
        style({ transform: 'translateY(-20px)', opacity: 0 }),
        animate('200ms ease-out', style({ transform: 'translateY(0)', opacity: 1 }))
      ]),
      transition(':leave', [
        animate('200ms ease-in', style({ transform: 'translateY(-20px)', opacity: 0 }))
      ])
    ])
  ]
})
export class ConfirmDialogComponent implements OnInit {
  dialogData: ConfirmDialogData | null = null;

  constructor(private confirmDialogService: ConfirmDialogService) {}

  ngOnInit(): void {
    this.confirmDialogService.dialogData$.subscribe(data => {
      this.dialogData = data;
    });
  }

  onConfirm(): void {
    if (this.dialogData?.onConfirm) {
      this.dialogData.onConfirm();
    }
    this.confirmDialogService.hide();
  }

  onCancel(): void {
    if (this.dialogData?.onCancel) {
      this.dialogData.onCancel();
    }
    this.confirmDialogService.hide();
  }
}
