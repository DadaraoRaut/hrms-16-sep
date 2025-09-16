import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-hr-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './hr-sidebar.component.html',
  styleUrls: ['./hr-sidebar.component.css']
})
export class HrSidebarComponent {
  isCollapsed = false;

  toggleSidebar() {
    this.isCollapsed = !this.isCollapsed;
  }


}
