import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css', './pagination.component.scss']
})
export class PaginationComponent implements OnInit {

  @Input() totalPages: number;
  @Input() currentPage: number;
  @Output() onPageSelected: EventEmitter<number>;
  constructor() {
    this.onPageSelected = new EventEmitter();
  }

  existNextPage():boolean {
    return this.currentPage+1 < this.totalPages;
  }

  ngOnInit() {
  }

  pageSelected(newPage: number) {
    if (newPage >= 0 && newPage <= this.totalPages) {
      this.currentPage = newPage;
      console.log(this.currentPage);
      this.onPageSelected.emit(newPage);
    }
  }

}