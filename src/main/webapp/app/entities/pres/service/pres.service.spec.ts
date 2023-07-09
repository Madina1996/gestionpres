import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPres } from '../pres.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pres.test-samples';

import { PresService } from './pres.service';

const requireRestSample: IPres = {
  ...sampleWithRequiredData,
};

describe('Pres Service', () => {
  let service: PresService;
  let httpMock: HttpTestingController;
  let expectedResult: IPres | IPres[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PresService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Pres', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pres = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pres).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pres', () => {
      const pres = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pres).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pres', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pres', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pres', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPresToCollectionIfMissing', () => {
      it('should add a Pres to an empty array', () => {
        const pres: IPres = sampleWithRequiredData;
        expectedResult = service.addPresToCollectionIfMissing([], pres);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pres);
      });

      it('should not add a Pres to an array that contains it', () => {
        const pres: IPres = sampleWithRequiredData;
        const presCollection: IPres[] = [
          {
            ...pres,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPresToCollectionIfMissing(presCollection, pres);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pres to an array that doesn't contain it", () => {
        const pres: IPres = sampleWithRequiredData;
        const presCollection: IPres[] = [sampleWithPartialData];
        expectedResult = service.addPresToCollectionIfMissing(presCollection, pres);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pres);
      });

      it('should add only unique Pres to an array', () => {
        const presArray: IPres[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const presCollection: IPres[] = [sampleWithRequiredData];
        expectedResult = service.addPresToCollectionIfMissing(presCollection, ...presArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pres: IPres = sampleWithRequiredData;
        const pres2: IPres = sampleWithPartialData;
        expectedResult = service.addPresToCollectionIfMissing([], pres, pres2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pres);
        expect(expectedResult).toContain(pres2);
      });

      it('should accept null and undefined values', () => {
        const pres: IPres = sampleWithRequiredData;
        expectedResult = service.addPresToCollectionIfMissing([], null, pres, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pres);
      });

      it('should return initial array if no Pres is added', () => {
        const presCollection: IPres[] = [sampleWithRequiredData];
        expectedResult = service.addPresToCollectionIfMissing(presCollection, undefined, null);
        expect(expectedResult).toEqual(presCollection);
      });
    });

    describe('comparePres', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePres(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePres(entity1, entity2);
        const compareResult2 = service.comparePres(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePres(entity1, entity2);
        const compareResult2 = service.comparePres(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePres(entity1, entity2);
        const compareResult2 = service.comparePres(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
