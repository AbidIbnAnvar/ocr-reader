// granularity-selector.component.ts
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TitleCasePipe } from '@angular/common';

@Component({
    imports: [TitleCasePipe],
    selector: 'app-granularity-selector',
    template: `
    <div class="flex gap-2">
      @for (option of options; track $index) {
        <button
        class="px-4 py-2 rounded-lg border transition"
        [class.bg-indigo-600]="option === selected"
        [class.text-white]="option === selected"
        [class.bg-gray-100]="option !== selected"
        (click)="select(option)"
      >
        {{ option | titlecase }}
      </button>
      }
    </div>
  `,
})
export class GranularitySelectorComponent {
    @Input() selected: 'day' | 'month' | 'year' = 'day';
    @Output() selectedChange = new EventEmitter<'day' | 'month' | 'year'>();

    options: ('day' | 'month' | 'year')[] = ['day', 'month', 'year'];

    select(option: 'day' | 'month' | 'year') {
        if (this.selected !== option) {
            this.selected = option;
            this.selectedChange.emit(option);
        }
    }
}
