package com.ediary.bootstrap;

import com.ediary.domain.*;
import com.ediary.domain.Class;
import com.ediary.domain.security.User;
import com.ediary.repositories.*;
import com.ediary.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultLoader implements CommandLineRunner {

    private final AddressRepository addressRepository;
    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentCouncilRepository parentCouncilRepository;
    private final StudentCouncilRepository studentCouncilRepository;
    private final ExtenuationRepository extenuationRepository;
    private final AttendanceRepository attendanceRepository;
    private final ClassRepository classRepository;

    private final Long firstUserAsStudentId = 2L;
    private final Long firstUserAsParentId = 9L;
    private final Long firstUserAsTeacherId = 16L;
    private Long firstStudentId;
    private Long firstParentId;

    private final int numberOfClasses = 3;
    private final int numberOfParents = 7;
    private final int numberOfStudents = 7;
    private final int numberOfTeachers = 6;

    @Override
    public void run(String... args) throws Exception {
        log.debug("-------------- Bootstrap --------------");

//        createAddresses();
        createSchool();
        createUsers();
        createStudentsAndParents();
        createTeachers();
        createParentCouncils();
        createStudentCouncils();
        createAttendances();
        createExtenuations();

    }

    private void createAddresses() {
        String state = "Poland";
        String[] cityNames = {"Szczecin"};
        String zip = "70-670";
        String[] streetNames = {"Odrodzenia", "Jagiellońska", "Kaszubska", "Jana Pawła", "Borysza",
                "Wielkopolska", "Podhalańska", "Odrodzenia", "Borysza", "Mazowiecka",
                "Kujawska", "Mazowiecka", "Wolności", "Wysoka", "Borysza"};

        String[] phoneNumber = {"502571821", "515556095", "505735402", "575120854", "455558291",
                "785152102", "735592154", "509124854", "571943111", "710430235",
                "509570183", "730852128", "515152532", "593561943", "920153853"};


        for(int i = 0; i < streetNames.length; i++) {
            addressRepository.save(Address.builder()
                    .id(addressRepository.count() + 1)
                    .state(state)
                    .city(cityNames[0])
                    .zip(zip)
                    .street(streetNames[i])
                    .phoneNumber(phoneNumber[i])
                    .build());
        }

        log.debug("-------- Created addresses: " + addressRepository.count());
    }



    private void createSchool() {
        schoolRepository.save(School.builder()
                .id(schoolRepository.count() + 1)
                .name("Darwin College")
                .headmasterName("Danuta")
                .headmasterLastName("Adamska")
                .email("d.adamska@dc.com")
                .schoolOffice("")
//                .address(addressRepository.findById(1L).orElse(null))
                .build());
    }

    private void createUsers() {

        //todo: username, password (with encoder)
        String[] studentNames = {"Dagmara", "Daniel", "Alicja", "Olgierd", "Kamil", "Olga", "Michał"};
        String[] studentLastNames = {"Lis", "Mazur", "Skrzeczkowska", "Szymczak", "Kubiak", "Woźniak", "Kalinowski"};

        String[] parentNames = {"Marian", "Ludwik", "Konrad", "Amir", "Irena", "Rafał", "Jan"};
        String[] parentLastNames = {"List", "Mazur", "Skrzeczkowski", "Szymczak", "Kubiak", "Woźniak", "Kalinowski"};

        String[] teacherNames = {"Czesław", "Rafał", "Maciej", "Magdalena", "Krzysztoł", "Katarzyna"};
        String[] teacherLastNames = {"Wójcik", "Marciniak", "Jankowski", "Kowalska", "Michalak", "Sikora"};



        //Students
        for (Long i = firstUserAsStudentId; i <= studentNames.length; i++) {
            userRepository.save(User.builder()
                    .id(i)
                    .firstName(studentNames[(i.intValue() % studentLastNames.length)])
                    .lastName(studentLastNames[(i.intValue() % studentLastNames.length)])
                    .username("")
                    .password("")
                    .build());
        }


        //Parents
        Long usersCount = userRepository.count();
        Long addressId = 2L;
        for (Long i = firstUserAsParentId; i <= (usersCount + parentLastNames.length); i++, addressId++) {
            userRepository.save(User.builder()
                    .id(i)
                    .firstName(parentNames[(i.intValue() % parentLastNames.length)])
                    .lastName(parentLastNames[(i.intValue() % parentLastNames.length)])
                    .username("")
                    .password("")
//                    .address(addressRepository.findById(addressId).orElse(null))
                    .build());

        }
        log.debug("-------- Created users: " + userRepository.count());

        //Teachers
        usersCount = userRepository.count();
        for (Long i = firstUserAsTeacherId; i <= (usersCount + teacherLastNames.length); i++) {
            userRepository.save(User.builder()
                    .id(i)
                    .firstName(teacherNames[(i.intValue() % teacherLastNames.length)])
                    .lastName(teacherLastNames[(i.intValue() % teacherLastNames.length)])
                    .username("")
                    .password("")
//                    .address(addressRepository.findById(addressId).orElse(null))
                    .build());
        }


        log.debug("-------- Created users: " + userRepository.count());


    }

    //todo: classes for student
    private void createStudentsAndParents() {

        long userAsStudentId = firstUserAsStudentId;
        long userAsParentId = firstUserAsParentId;

        for (; userAsStudentId < firstUserAsStudentId + numberOfStudents; userAsStudentId++, userAsParentId++) {
            studentRepository.save(Student.builder()
                    .id((studentRepository.count() + 1L))
                    .user(userRepository.findById(userAsStudentId).orElse(null))
                    .parent(createParent(parentRepository.count() + 1, userAsParentId))
                    .build());
        }


        firstStudentId = studentRepository.findByUserId(firstUserAsStudentId).getId();
        firstParentId = parentRepository.findByUserId(firstUserAsParentId).getId();

        log.debug("-------- Created students: " + studentRepository.count());
        log.debug("-------- Created parents: " + parentRepository.count());

    }


    //todo: extenuation,
    private Parent createParent(Long id, Long userId) {
        return parentRepository.save(Parent.builder()
                .id(id)
                .user(userRepository.findById(userId).orElse(null))
                .build());
    }


    private void createTeachers() {

        for (long userAsTeacherId = firstUserAsTeacherId; userAsTeacherId < firstUserAsTeacherId + numberOfTeachers; userAsTeacherId++) {
            teacherRepository.save(Teacher.builder()
                    .id(teacherRepository.count() + 1)
                    .user(userRepository.findById(userAsTeacherId).orElse(null))
                    .build());
        }

        log.debug("-------- Created teachers: " + teacherRepository.count());

    }


    private void createParentCouncils() {
        for(int i = 0; i < numberOfClasses; i++) {
            parentCouncilRepository.save(ParentCouncil.builder()
                    .id(parentCouncilRepository.count() + 1)
                    .build());
        }
    }


    private void createStudentCouncils() {
        for(int i = 0; i < numberOfClasses; i++) {
            studentCouncilRepository.save(StudentCouncil.builder()
                    .id(studentCouncilRepository.count() + 1)
                    .build());
        }
    }





    private void createExtenuations() {

        Long firstParentId = this.firstParentId;

        //Extenuations -> Parent id = 8
        extenuationRepository.save(Extenuation.builder()
                .id(extenuationRepository.count() + 1)
                .parent(parentRepository.findById(firstParentId).orElse(null))
                .status(Extenuation.Status.SENT)
                .build());


        ////Extenuations -> Parent id = 9
        extenuationRepository.save(Extenuation.builder()
                .id(extenuationRepository.count() + 1)
                .parent(parentRepository.findById(firstParentId + 1).orElse(null))
                .status(Extenuation.Status.SENT)
                .build());

        log.debug("Created extenuations: " + extenuationRepository.count());
    }



    //todo: lesson
    private void createAttendances() {

        Long firstStudentId = this.firstStudentId;

        //Attendances for each students
        Attendance.Status[] statuses = {
                Attendance.Status.ABSENT,
                Attendance.Status.ABSENT,
                Attendance.Status.PRESENT,
                Attendance.Status.LATE,
                Attendance.Status.PRESENT,
                Attendance.Status.EXCUSED,
                Attendance.Status.ABSENT,
                Attendance.Status.PRESENT
        };

        for(Long userAsStudentId = firstUserAsStudentId; userAsStudentId < firstUserAsParentId; userAsStudentId++) {
            attendanceRepository.save(Attendance.builder()
                    .id(attendanceRepository.count() + 1)
                    .student(studentRepository.findById(firstStudentId++).orElse(null))
                    .status(statuses[userAsStudentId.intValue() - 1])
                    .build());

        }

        log.debug("Created attendances: " + attendanceRepository.count());
    }





}

