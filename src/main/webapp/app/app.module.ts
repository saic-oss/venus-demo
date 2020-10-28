import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { ExampleProjectSharedModule } from 'app/shared/shared.module';
import { ExampleProjectCoreModule } from 'app/core/core.module';
import { ExampleProjectAppRoutingModule } from './app-routing.module';
import { ExampleProjectHomeModule } from './home/home.module';
import { ExampleProjectEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    ExampleProjectSharedModule,
    ExampleProjectCoreModule,
    ExampleProjectHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    ExampleProjectEntityModule,
    ExampleProjectAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class ExampleProjectAppModule {}
