import { TestBed } from '@angular/core/testing';

import { InWebDataService } from './in-web-data.service';

describe('InWebDataService', () => {
  let service: InWebDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InWebDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
