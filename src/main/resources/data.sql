
INSERT INTO locations (id, code, name, address)
VALUES
                                                    (1, '1', 'İstanbul Airport', 'Address 1'),
                                                    (2, '2', 'İstanbul Sabiha Gökçen Airport', 'Address 2');

INSERT INTO services (id, name, price) VALUES
                                           (1, 'Additional Driver', 50.00),
                                           (2, 'Towing Service', 70.00);

INSERT INTO equipments (id, name, price) VALUES
                                             (1, 'GPS', 15.00),
                                             (2, 'Child Seat', 10.00);

INSERT INTO cars (
    id,
    barcode_number,
    license_plate_number,
    passenger_capacity,
    brand,
    model,
    mileage,
    transmission_type,
    daily_price,
    status,
    car_type
) VALUES (
             1,
             'CAR123',
             '34ABC34',
             4,
             'Toyota',
             'Corolla',
             10000,
             'AUTOMATIC',
             150.00,
             'AVAILABLE',
             'ECONOMY'
         );

INSERT INTO member (
    id,
    name,
    address,
    email,
    phone,
    driving_license_number
) VALUES (
             1,
             'John Doe',
             '123 Main St',
             'john.doe@example.com',
             '555-1234',
             'DL123456'
         );


INSERT INTO reservations (
    id,
    reservation_number,
    creation_date,
    pick_up_date_time,
    drop_off_date_time,
    return_date,
    status,
    day_count,
    member_id,
    car_id,
    pick_up_location_id,
    drop_off_location_id
) VALUES (
             1,
             'RES12345',
             CURRENT_TIMESTAMP,
             DATEADD('DAY', 1, CURRENT_TIMESTAMP),
             DATEADD('DAY', 3, CURRENT_TIMESTAMP),
             NULL,
             'ACTIVE',
             2,
             1,  -- member_id
             1,  -- car_id
             1,  -- pick_up_location_id
             2   -- drop_off_location_id
         );

INSERT INTO reservation_service (reservation_id, service_id) VALUES
                                                                 (1, 1),
                                                                 (1, 2);

INSERT INTO reservation_equipment (reservation_id, equipment_id) VALUES
                                                                     (1, 1),
                                                                     (1, 2);