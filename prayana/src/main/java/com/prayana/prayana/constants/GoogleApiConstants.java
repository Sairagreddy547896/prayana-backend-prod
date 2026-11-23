package com.prayana.prayana.constants;

public final class GoogleApiConstants {

    private GoogleApiConstants(){}

    public static final String HEADER_API_KEY="X-Goog-Api-Key";
    public static final String HEADER_FIELD_MASK="X-Goog-FieldMask";

    public static final String SEARCH_FIELD_MASK=
                    "places.id,"    +
                    "places.displayName,"   +
                    "places.types," +
                    "places.formattedAddress,"  +
                    "places.location,"   +
                    "places.regularOpeningHours,"   +
                    "places.nationalPhoneNumber,"   +
                    "places.googleMapsUri,"     +
                    "places.addressComponents";

}
