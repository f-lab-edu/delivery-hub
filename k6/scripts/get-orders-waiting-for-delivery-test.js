import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '10m', target: 1500 },
    ],
};

const validLocations = [
    { city: '울산광역시',  district: '중구' },
    { city: '울산광역시',  district: '남구' },
    { city: '서울특별시',  district: '서초구' },
    { city: '경기도',      district: '수원시 장안구' },
    { city: '부산광역시',  district: '수영구' },
    { city: '경기도',      district: '고양시 일산동구' },
    { city: '부산광역시',  district: '부산진구' },
    { city: '광주광역시',  district: '서구' },
    { city: '대전광역시',  district: '유성구' },
    { city: '광주광역시',  district: '남구' },
    { city: '충청남도',    district: '공주시' },
    { city: '대전광역시',  district: '서구' },
    { city: '전라북도',    district: '전주시 완산구' },
    { city: '인천광역시',  district: '부평구' },
    { city: '부산광역시',  district: '동래구' },
    { city: '대구광역시',  district: '수성구' },
    { city: '인천광역시',  district: '연수구' },
    { city: '서울특별시',  district: '송파구' },
    { city: '경기도',      district: '성남시 분당구' },
    { city: '서울특별시',  district: '강남구' },
    { city: '부산광역시',  district: '해운대구' },
    { city: '전라북도',    district: '군산시' },
    { city: '충청남도',    district: '천안시 동남구' },
    { city: '인천광역시',  district: '남동구' },
    { city: '서울특별시',  district: '종로구' },
    { city: '대구광역시',  district: '중구' },
];

function pad2(n) {
    return n < 10 ? '0' + n : '' + n;
}

function pad3(n) {
    if (n < 10) return '00' + n;
    if (n < 100) return '0' + n;
    return '' + n;
}

export default function () {
    const idx = Math.floor(Math.random() * validLocations.length);
    const { city, district } = validLocations[idx];

    const lower = new Date(2025, 1, 4, 6, 22, 52, 0);
    const upper = new Date(2025, 4, 31, 0, 9, 31, 0);

    const lowerMs = lower.getTime();
    const upperMs = upper.getTime();
    const randomMs = Math.floor(Math.random() * (upperMs - lowerMs)) + lowerMs;
    const randDate = new Date(randomMs);

    const year   = randDate.getFullYear();
    const month  = pad2(randDate.getMonth() + 1);
    const day    = pad2(randDate.getDate());
    const hour   = pad2(randDate.getHours());
    const minute = pad2(randDate.getMinutes());
    const second = pad2(randDate.getSeconds());
    const milli  = pad3(randDate.getMilliseconds());
    const lastCreatedAt = `${year}-${month}-${day}T${hour}:${minute}:${second}.${milli}000`;

    const page = 0;
    const size = 10;

    const baseURL = 'http://host.docker.internal:8080';
    const url =
        `${baseURL}/orders/paid?` +
        `city=${encodeURIComponent(city)}` +
        `&district=${encodeURIComponent(district)}` +
        `&lastCreatedAt=${encodeURIComponent(lastCreatedAt)}` +
        `&page=${page}` +
        `&size=${size}`;

    const res = http.get(url);
    sleep(1);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });
}
