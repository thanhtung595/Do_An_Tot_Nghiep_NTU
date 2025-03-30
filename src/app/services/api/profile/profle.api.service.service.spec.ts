import { TestBed } from '@angular/core/testing';

import { ProfleApiServiceService } from './profle.api.service.service';

describe('ProfleApiServiceService', () => {
  let service: ProfleApiServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProfleApiServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
