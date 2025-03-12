import { TestBed } from '@angular/core/testing';

import { HeaderApiServiceService } from './header.api.service.service';

describe('HeaderApiServiceService', () => {
  let service: HeaderApiServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HeaderApiServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
