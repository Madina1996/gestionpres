import { Component, OnInit } from '@angular/core';

import { AccountService } from 'app/core/auth/account.service';
import { AppPageTitleStrategy } from 'app/app-page-title-strategy';
import { Router } from '@angular/router';
import { Account } from '../../core/auth/account.model';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  styleUrls: ['./sidebare.component.scss'],
  providers: [AppPageTitleStrategy],
})
export default class MainComponent implements OnInit {
  constructor(private router: Router, private appPageTitleStrategy: AppPageTitleStrategy, private accountService: AccountService) {}
  account: Account | null = null;
  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
  }
}
