import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecentReceiptsComponent } from './recent-receipts.component';

describe('RecentReceiptsComponent', () => {
  let component: RecentReceiptsComponent;
  let fixture: ComponentFixture<RecentReceiptsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecentReceiptsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecentReceiptsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
