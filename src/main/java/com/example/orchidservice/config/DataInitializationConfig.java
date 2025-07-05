package com.example.orchidservice.config;

import com.example.orchidservice.pojo.*;
import com.example.orchidservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializationConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(
            RoleRepository roleRepository,
            AccountRepository accountRepository,
            CategoryRepository categoryRepository,
            OrchidRepository orchidRepository
    ) {
        return args -> {

            // Roles
            Role superAdmin = roleRepository.findById("1")
                    .orElseGet(() -> roleRepository.save(new Role("1", "SuperAdmin")));
            roleRepository.findById("2")
                    .orElseGet(() -> roleRepository.save(new Role("2", "Admin")));
            roleRepository.findById("3")
                    .orElseGet(() -> roleRepository.save(new Role("3", "Customer")));

            // SuperAdmin account
            accountRepository.findByEmail("superadmin@gmail.com")
                    .orElseGet(() -> {
                        Account acc = new Account();
                        acc.setEmail("superadmin@gmail.com");
                        acc.setPassword(passwordEncoder.encode("123456"));
                        acc.setAccountName("SuperAdmin");
                        acc.setRole(roleRepository.findById("1").get());
                        return accountRepository.save(acc);
                    });
            accountRepository.findByEmail("admin@gmail.com")
                    .orElseGet(() -> {
                                Account acc = new Account();
                                acc.setEmail("admin@gmail.com");
                                acc.setPassword(passwordEncoder.encode("123456"));
                                acc.setAccountName("Admin");
                                acc.setRole(roleRepository.findById("2").get());
                                return accountRepository.save(acc);
                            });

            // Categories
// Category - không cần truyền orchids list nữa
            if (categoryRepository.count() == 0) {
                categoryRepository.saveAll(List.of(
                        new Category("1", "Popular"),
                        new Category("2", "Thai Orchid"),
                        new Category("3", "Miniature"),
                        new Category("4", "Premium"),
                        new Category("5", "Macro View"),
                        new Category("6", "Event Flower")
                ));
            }


            // Orchids
            if (orchidRepository.count() == 0) {
                List<Orchid> orchids = new ArrayList<>();

                orchids.add(new Orchid(null, true, "Elegant pink Phalaenopsis orchid, perfect for indoor decor.", "Phalaenopsis Pink",
                        "https://images.unsplash.com/photo-1567957452651-302037461536?q=80...", "1250000", categoryRepository.findById("1").get(), null));

                orchids.add(new Orchid(null, true, "Vibrant purple Thai orchid known for its longevity and fragrance.", "Thai Orchid",
                        "https://cdn.pixabay.com/photo/2017/06/18/16/29/orchid-2416323_1280.jpg", "2325000", categoryRepository.findById("2").get(), null));

                orchids.add(new Orchid(null, true, "Delicate miniature orchid ideal for small pots and terrariums.", "Miniature Orchid",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/1/10/Little_orchid2.jpg/1200px-Little_orchid2.jpg?20091022203919", "2500000", categoryRepository.findById("3").get(), null));

                orchids.add(new Orchid(null, true, "A unique speckled orchid with soft white petals and pink core.", "Orchid D1102a",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Little_orchid_D1102a.jpg/1200px-Little_orchid_D1102a.jpg?20121011205623", "475000", categoryRepository.findById("4").get(), null));

                orchids.add(new Orchid(null, true, "Pastel-colored orchid with calming shades for relaxation spaces.", "Orchid D1102b",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Little_orchid_D1102a.jpg/1200px-Little_orchid_D1102a.jpg?20121011205623", "925000", categoryRepository.findById("4").get(), null));

                orchids.add(new Orchid(null, true, "Stunning bloom with subtle hues of peach and pink.", "Orchid D1205b",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/9/90/Little_orchid_D1205b.jpg/1200px-Little_orchid_D1205b.jpg?20121019205734", "625000", categoryRepository.findById("4").get(), null));

                orchids.add(new Orchid(null, true, "Compact orchid variety with long-lasting violet flowers.", "Orchid D1304",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Little_orchid_D1304.jpg/1200px-Little_orchid_D1304.jpg?20130711212555", "1150000", categoryRepository.findById("4").get(), null));

                orchids.add(new Orchid(null, true, "Graceful purple orchid with large, flat petals and rich texture.", "Delicate Purple Orchid",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSXn9UTe7FuoTz2tezGObR-nKJBEJFBP_hB4w&s", "1500000", categoryRepository.findById("4").get(), null));

                orchids.add(new Orchid(null, true, "Classic white Phalaenopsis orchid, symbolizes purity and elegance.", "Phalaenopsis White",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/9/96/Falenopsis_Flower.jpg/1200px-Falenopsis_Flower.jpg?20081114183219", "1200000", categoryRepository.findById("1").get(), null));

                orchids.add(new Orchid(null, true, "Unique flower with face-like petal patterns, ideal for gifts.", "Orchid Face Bloom",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Flower_face.jpg/1062px-Flower_face.jpg?20100625144604", "1300000", categoryRepository.findById("4").get(), null));

                orchids.add(new Orchid(null, true, "Locally grown orchid, hardy and blooms several times a year.", "Orchid My House",
                        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8rGHNgen6y1tozR90Xygp98eLClJCkh4Q2w&s", "1987000", categoryRepository.findById("4").get(), null));

                orchids.add(new Orchid(null, true, "Orchid from Thai highlands, bright magenta with green streaks.", "Thai Jungle Orchid",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a2/Kingdom_Plantae_in_Thailand_Photographed_by_Peak_Hora_%2863%29.jpg/1200px-Kingdom_Plantae_in_Thailand_Photographed_by_Peak_Hora_%2863%29.jpg?20181219012947", "2035000", categoryRepository.findById("2").get(), null));

                orchids.add(new Orchid(null, true, "A hybrid orchid from Italy with deep purple velvet petals.", "La Mortella Orchid",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/0/01/La_Mortella_orqu%C3%ADdeas_07.JPG/1200px-La_Mortella_orqu%C3%ADdeas_07.JPG?20140817215621", "506000", categoryRepository.findById("4").get(), null));

                orchids.add(new Orchid(null, true, "Light pink orchid with subtle curves and a sweet aroma.", "Orchid Light Pink D1212",
                        "https://upload.wikimedia.org/wikipedia/commons/c/cf/Little_orchid_D1212_light_pink.jpg", "1700000", categoryRepository.findById("4").get(), null));

                orchids.add(new Orchid(null, true, "Bright pink petals with vibrant veining and a delicate look.", "Orchid Pink D1212",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/7/70/Little_orchid_D1212_pink.jpg/1200px-Little_orchid_D1212_pink.jpg?20130118203304", "1345000", categoryRepository.findById("4").get(), null));

                orchids.add(new Orchid(null, true, "Elegant orchid with blush pink petals and a yellow throat.", "Orchid D1306",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/Little_orchid_D1306_pink_flower.jpg/1200px-Little_orchid_D1306_pink_flower.jpg?20131206203755", "2000000", categoryRepository.findById("4").get(), null));

                orchids.add(new Orchid(null, true, "Cluster of cascading white orchids perfect for centerpieces.", "White Orchid Cascade",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/b/be/Many_white_orchids.jpg/1200px-Many_white_orchids.jpg?20231231191912", "800000", categoryRepository.findById("6").get(), null));

                orchids.add(new Orchid(null, true, "Close-up orchid showing intricate petal structure and color.", "Orchid Macro Shot",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Orchid_%288680402926%29.jpg/1200px-Orchid_%288680402926%29.jpg?20130429140658", "500000", categoryRepository.findById("5").get(), null));

                orchids.add(new Orchid(null, true, "Magnified view of orchid center with deep maroon detailing.", "Ultra Close Orchid",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/Orchid.._Ultra_close-up_%284607063313%29.jpg/1200px-Orchid.._Ultra_close-up_%284607063313%29.jpg?20130908142650", "780000", categoryRepository.findById("5").get(), null));

                orchids.add(new Orchid(null, true, "Elegant white orchid with soft yellow core, great for events.", "Phalaenopsis H8a",
                        "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bb/Orchidee_Phalaenopsis_H8a.jpg/1197px-Orchidee_Phalaenopsis_H8a.jpg?20220313085847", "2936000", categoryRepository.findById("1").get(), null));

                orchidRepository.saveAll(orchids);
            }
        };
    }
}
