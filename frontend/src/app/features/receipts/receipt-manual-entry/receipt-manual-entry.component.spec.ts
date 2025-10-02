import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceiptManualEntryComponent } from './receipt-manual-entry.component';

describe('ReceiptManualEntryComponent', () => {
  let component: ReceiptManualEntryComponent;
  let fixture: ComponentFixture<ReceiptManualEntryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReceiptManualEntryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReceiptManualEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
