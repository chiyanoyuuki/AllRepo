import { TestBed } from '@angular/core/testing';

import { TstService } from './tst.service';

describe('TstService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TstService = TestBed.get(TstService);
    expect(service).toBeTruthy();
  });
});
