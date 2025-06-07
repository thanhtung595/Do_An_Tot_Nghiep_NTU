import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ConfirmDialogData } from '@app/model/confirm-dialog.model';

@Injectable({
  providedIn: 'root'
})
export class ConfirmDialogService {
  private dialogData = new BehaviorSubject<ConfirmDialogData | null>(null);
  dialogData$ = this.dialogData.asObservable();

  show(data: ConfirmDialogData): void {
    this.dialogData.next(data);
  }

  hide(): void {
    this.dialogData.next(null);
  }
}
