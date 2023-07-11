import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { InputTextModule } from 'primeng/inputtext';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import { ToolbarModule } from 'primeng/toolbar';
import { DataViewModule } from 'primeng/dataview';
import { DropdownModule } from 'primeng/dropdown';
import { DockModule } from 'primeng/dock';
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MenuModule } from 'primeng/menu';
import { CarouselModule } from 'primeng/carousel';
import { TableModule } from 'primeng/table';
import { StyleClassModule } from 'primeng/styleclass';
import { PanelMenuModule } from 'primeng/panelmenu';
import { AvatarModule } from 'primeng/avatar';
import { AvatarGroupModule } from 'primeng/avatargroup';
import { FieldsetModule } from 'primeng/fieldset';
import { DividerModule } from 'primeng/divider';
import { KnobModule } from 'primeng/knob';
import { CompteService } from '../entities/compte/service/compte.service';
import { FournisseurService } from '../entities/fournisseur/service/fournisseur.service';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  imports: [
    SharedModule,
    RouterModule,
    InputTextModule,
    DialogModule,
    TagModule,
    ToolbarModule,
    DataViewModule,
    DropdownModule,
    DockModule,
    CardModule,
    ButtonModule,
    FormsModule,
    CommonModule,
    MenuModule,
    CarouselModule,
    TableModule,
    StyleClassModule,
    PanelMenuModule,
    AvatarModule,
    AvatarGroupModule,
    FieldsetModule,
    DividerModule,
    KnobModule,
  ],
})
export default class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  montantdu: any;
  depense: any;
  fournisseurs: any = [];

  compte: any;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private compteService: CompteService,
    private fournisseurService: FournisseurService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.account = account;
        console.log(account);
        if (account === null) {
          this.router.navigate(['/login']);
        }
      });

    this.fournisseurService.query().subscribe(res => {
      console.log(res);
      this.fournisseurs = res.body;
    });
    this.compteService.findInfos().subscribe(res => {
      console.log(res.body);
      this.compte = res.body;
    });

    if (this.account === null) {
      this.router.navigate(['/login']);
    }
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
