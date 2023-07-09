import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IRembourser } from '../rembourser.model';
import { RembourserService } from '../service/rembourser.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './rembourser-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RembourserDeleteDialogComponent {
  rembourser?: IRembourser;

  constructor(protected rembourserService: RembourserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rembourserService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
