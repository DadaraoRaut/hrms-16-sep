import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DEIComponent } from './dei.component';

describe('DEIComponent', () => {
  let component: DEIComponent;
  let fixture: ComponentFixture<DEIComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DEIComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DEIComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
