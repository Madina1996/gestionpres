import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRembourser } from '../rembourser.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../rembourser.test-samples';

import { RembourserService } from './rembourser.service';

const requireRestSample: IRembourser = {
  ...sampleWithRequiredData,
};

describe('Rembourser Service', () => {
  let service: RembourserService;
  let httpMock: HttpTestingController;
  let expectedResult: IRembourser | IRembourser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RembourserService);
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

    it('should create a Rembourser', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const rembourser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(rembourser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Rembourser', () => {
      const rembourser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(rembourser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Rembourser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Rembourser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Rembourser', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRembourserToCollectionIfMissing', () => {
      it('should add a Rembourser to an empty array', () => {
        const rembourser: IRembourser = sampleWithRequiredData;
        expectedResult = service.addRembourserToCollectionIfMissing([], rembourser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rembourser);
      });

      it('should not add a Rembourser to an array that contains it', () => {
        const rembourser: IRembourser = sampleWithRequiredData;
        const rembourserCollection: IRembourser[] = [
          {
            ...rembourser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRembourserToCollectionIfMissing(rembourserCollection, rembourser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Rembourser to an array that doesn't contain it", () => {
        const rembourser: IRembourser = sampleWithRequiredData;
        const rembourserCollection: IRembourser[] = [sampleWithPartialData];
        expectedResult = service.addRembourserToCollectionIfMissing(rembourserCollection, rembourser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rembourser);
      });

      it('should add only unique Rembourser to an array', () => {
        const rembourserArray: IRembourser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const rembourserCollection: IRembourser[] = [sampleWithRequiredData];
        expectedResult = service.addRembourserToCollectionIfMissing(rembourserCollection, ...rembourserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rembourser: IRembourser = sampleWithRequiredData;
        const rembourser2: IRembourser = sampleWithPartialData;
        expectedResult = service.addRembourserToCollectionIfMissing([], rembourser, rembourser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rembourser);
        expect(expectedResult).toContain(rembourser2);
      });

      it('should accept null and undefined values', () => {
        const rembourser: IRembourser = sampleWithRequiredData;
        expectedResult = service.addRembourserToCollectionIfMissing([], null, rembourser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rembourser);
      });

      it('should return initial array if no Rembourser is added', () => {
        const rembourserCollection: IRembourser[] = [sampleWithRequiredData];
        expectedResult = service.addRembourserToCollectionIfMissing(rembourserCollection, undefined, null);
        expect(expectedResult).toEqual(rembourserCollection);
      });
    });

    describe('compareRembourser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRembourser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRembourser(entity1, entity2);
        const compareResult2 = service.compareRembourser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRembourser(entity1, entity2);
        const compareResult2 = service.compareRembourser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRembourser(entity1, entity2);
        const compareResult2 = service.compareRembourser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
