package com.antoniuswicaksana.project_pbp;

import java.util.ArrayList;

public class DaftarEvent {
    public ArrayList<Event> EVENT;

    public DaftarEvent(){
        EVENT = new ArrayList();
        EVENT.add(WEDDING_A);
        EVENT.add(WEDDING_B);
        EVENT.add(WEDDING_C);
        EVENT.add(BIRTHDAY_A);
        EVENT.add(BIRTHDAY_B);
        EVENT.add(KONSER_A);
        EVENT.add(KONSER_B);
    }

    public static final Event WEDDING_A = new Event("Wedding Paket A", 100000000,
            "100", "https://thenewtimespress.com/wp-content/uploads/2015/02/Big-wedding-1-400x261.jpg");

    public static final Event WEDDING_B = new Event("Wedding Paket B", 70000000,
            "70", "https://lh3.googleusercontent.com/proxy/Eedauj8VEPw7GdXojfiV70VfXy-a8YIIst2akZbK3fcTiHdHwVTlmYkYwYv8Z2fwU_O2CRr-9ta4szhO6Ahq_xoGe9RQAdSplhF0wmPHU1UBMkcnTJWTYcuadMTklh4txqesCHA");

    public static final Event WEDDING_C = new Event("Wedding Paket C", 50000000,
            "50", "https://www.villabaroncino.com/wp-content/uploads/2016/10/Jen-Brad_1083-R-1.jpg");

    public static final Event BIRTHDAY_A = new Event("Birthday Paket A", 15000000,
            "50", "https://images.squarespace-cdn.com/content/v1/56deb2a686db43d6d09375d3/1479782543078-VKB94YZLTBGQMEYDZ022/ke17ZwdGBToddI8pDm48kBsRZsWpvBnYzI-iarZFLgR7gQa3H78H3Y0txjaiv_0fDoOvxcdMmMKkDsyUqMSsMWxHk725yiiHCCLfrh8O1z5QPOohDIaIeljMHgDF5CVlOqpeNLcJ80NK65_fV7S1Ubpie9_Y7lJ4yJQ0QRm0zlhNxqyTtsm2FQlmunf8n0EsOAAoc3o4sQuJA5bg0uLeTA/11-birthday-party.w750.h560.2x.jpg?format=1500w");

    public static final Event BIRTHDAY_B = new Event("Birthday Paket B", 10000000,
            "30", "https://www.partybarnrentme.com/wp-content/uploads/2017/12/Birthday-Party.jpeg");

    public static final Event KONSER_A = new Event("Konser Paket A", 100000000,
            "1000", "https://i.guim.co.uk/img/media/ad98f2dc808f18131e35e59c05ba6212671e8227/94_0_3061_1838/master/3061.jpg?width=1200&quality=85&auto=format&fit=max&s=fe0f9ee06af06c3c9f4b88c6dfd87b7b");

    public static final Event KONSER_B = new Event("Konser Paket B", 50000000,
            "500", "https://api.time.com/wp-content/uploads/2020/08/taipei-concert-coronavirus-eric-chou-anrong-xu-006.jpg?quality=85&crop=0px%2C267px%2C2400px%2C1256px&resize=1200%2C628&strip");
}
