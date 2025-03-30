import { TestBed } from '@angular/core/testing';

import { DepartmentsApiService } from './departments.api.service';

describe('DepartmentsApiService', () => {
  let service: DepartmentsApiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DepartmentsApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
