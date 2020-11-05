import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { VenusdemoSharedModule } from 'app/shared/shared.module';
import { VenusdemoCoreModule } from 'app/core/core.module';
import { VenusdemoAppRoutingModule } from './app-routing.module';
import { VenusdemoHomeModule } from './home/home.module';
import { VenusdemoEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    VenusdemoSharedModule,
    VenusdemoCoreModule,
    VenusdemoHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    VenusdemoEntityModule,
    VenusdemoAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class VenusdemoAppModule {}
