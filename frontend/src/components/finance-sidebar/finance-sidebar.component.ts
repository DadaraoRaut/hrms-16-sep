import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-finance-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './finance-sidebar.component.html',
  styleUrls: ['./finance-sidebar.component.css']
})
export class FinanceSidebarComponent {
  isCollapsed = false;

  toggleSidebar() {
    this.isCollapsed = !this.isCollapsed;
  }
}
