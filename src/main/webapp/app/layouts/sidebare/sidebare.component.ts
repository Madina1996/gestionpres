import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from '../../core/auth/account.model';
import { RouterModule } from '@angular/router';
import SharedModule from '../../shared/shared.module';
import HasAnyAuthorityDirective from '../../shared/auth/has-any-authority.directive';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from '../../app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MenubarModule } from 'primeng/menubar';
import { PanelMenuModule } from 'primeng/panelmenu';
import { InputTextModule } from 'primeng/inputtext';
import { AvatarModule } from 'primeng/avatar';
import { StepsModule } from 'primeng/steps';
import { ToastModule } from 'primeng/toast';
import { CardModule } from 'primeng/card';
import { FileUploadModule } from 'primeng/fileupload';
import { ScrollPanelModule } from 'primeng/scrollpanel';

@Component({
  standalone: true,
  selector: 'jhi-sidebare',
  templateUrl: './sidebare.component.html',
  styleUrls: ['./sidebare.component.scss'],
  imports: [
    RouterModule,
    SharedModule,
    HasAnyAuthorityDirective,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MenubarModule,
    PanelMenuModule,
    InputTextModule,
    AvatarModule,
    StepsModule,
    ToastModule,
    CardModule,
    FileUploadModule,
    ScrollPanelModule,
  ],
})
export default class SidebareComponent implements OnInit {
  items: MenuItem[] = [];
  account: Account | null = null;

  constructor(private accountService: AccountService) {}

  ngOnInit(): void {
    console.log('dans ngOnInit');

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      if (account !== null) {
        console.log('dans if');
        this.items = [
          // {
          //   label: 'Statistique',
          //   routerLink: ['perpectiveglobal'],
          //   icon: 'pi pi-pw pi-chart-line',
          //   items: [
          //     { label: 'Region', icon: 'pi pi-fw pi-chart-bar', routerLink: ['perpectiveregion'] },
          //     { separator: true },
          //     { label: 'Programme', icon: 'pi pi-fw pi-globe', routerLink: ['perpectiveprogramme'] },
          //     { separator: true },
          //     /*   { label: 'Secteur', icon: 'pi pi-fw pi-chart-pie', routerLink: ['responsable/encour'] },
          //     { separator: true },
          //     { label: 'Sexe', icon: 'pi pi-fw pi-users', routerLink: ['responsable/affecter'] },
          //     { label: 'Age', icon: 'pi pi-fw pi-user', routerLink: ['responsable/valide'] }, */
          //   ],
          // },
          {
            label: 'Pres',
            icon: 'pi pi-pw pi-th-large',
            routerLink: ['pres'],
            // items: [
            //   { label: 'Tous les dossiers', routerLink: ['responsable'] },
            //   { separator: true },
            //   { label: 'En attente', icon: 'pi pi-fw pi-file-edit', routerLink: ['responsable/encour'] },
            //   { separator: true },
            //   { label: 'Affecter', icon: 'pi pi-fw pi-file-export', routerLink: ['responsable/affecter'] },
            //   { label: 'Valide', icon: 'pi pi-fw pi-check', routerLink: ['responsable/valide'] },
            //   { label: 'Financés', icon: 'pi pi-fw pi-money-bill', routerLink: ['responsable/finance'] },
            // ],
          },

          {
            label: 'Despense',
            routerLink: ['depannage'],
            icon: 'pi pi-fw pi-wrench',
          },
          {
            label: 'Rembourser',
            routerLink: ['voiture'],
            icon: 'pi pi-fw pi-truck',
          },
          {
            label: 'Fournisseur',
            routerLink: ['chauffeur'],
            icon: 'pi pi-fw pi-users',
          },
          {
            label: 'Activites',
            icon: 'pi pi-fw pi-cog',
            items: [{ label: 'Affectation', routerLink: ['conduire/new'] }, { separator: true }],
          },
        ];
      }
    });
  }
}
