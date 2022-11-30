import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatDividerModule} from '@angular/material/divider';
import {MatIconModule} from '@angular/material/icon';
import { MatOptionModule } from '@angular/material/core';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatSelectModule } from '@angular/material/select';
import {MatCardModule} from '@angular/material/card';
import {MatMenuModule} from '@angular/material/menu';
import {MatListModule} from '@angular/material/list';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatTabsModule} from '@angular/material/tabs';
import {MatDialogModule} from '@angular/material/dialog';
import {MatStepperModule} from '@angular/material/stepper';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';

const MaterialConstants = [
  MatButtonModule,
  MatInputModule,
  MatFormFieldModule,
  MatGridListModule,
  MatDividerModule,
  MatIconModule,
  MatOptionModule,
  MatAutocompleteModule,
  MatCheckboxModule,
  MatSelectModule,
  MatCardModule,
  MatMenuModule,
  MatListModule,
  MatExpansionModule,
  MatIconModule,
  MatToolbarModule,
  MatIconModule,
  MatTabsModule,
  MatDialogModule,
  MatStepperModule,
  MatTooltipModule,
  MatSlideToggleModule
]

@NgModule({
  imports: [MaterialConstants],
  exports: [MaterialConstants]
})
export class MaterialModule { }
