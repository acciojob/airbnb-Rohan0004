package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class HotelManagementRepository {

    Map<String,Hotel> hotelMap = new HashMap<>();
    Map<Integer,User> userMap = new HashMap<>();

    Map<String,Booking> bookingMap=new HashMap<>();
    public boolean addHotel(Hotel hotel) {
        if(hotel ==null || hotel.getHotelName()==null || hotelMap.containsKey(hotel.getHotelName())) return false;
        hotelMap.put(hotel.getHotelName(), hotel);
        return true;
    }

    public Integer addUser(User user) {
        userMap.put(user.getaadharCardNo(),user);
        return user.getaadharCardNo();
    }

    public String getHotelWithMostFacilities() {
        String res="";
        int facilities=0;
        for (Hotel hotel:hotelMap.values()) {
            int cnt=hotel.getFacilities().size();
            if(cnt>facilities){
                facilities=cnt;
                res=hotel.getHotelName();
            }else if (cnt==facilities && hotel.getHotelName().compareTo(res)<0){
                res=hotel.getHotelName();
            }
        }
        return res;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        List<Facility> facilities = hotelMap.get(hotelName).getFacilities();
        for (Facility facility:newFacilities) {
            if (!facilities.contains(facility)) facilities.add(facility);
        }
        Hotel hotel=hotelMap.get(hotelName);
        hotel.setFacilities(facilities);
        return hotel;
    }

    public int bookARoom(Booking booking) {
        int aadharId=booking.getBookingAadharCard();
        String person=booking.getBookingPersonName();
        String hotel=booking.getHotelName();
        if(hotelMap.get(hotel)==null || hotelMap.get(hotel).getAvailableRooms()<booking.getNoOfRooms()) return -1;

        String uuid = UUID.randomUUID().toString();
        int bill = booking.getNoOfRooms() * hotelMap.get(hotel).getPricePerNight();
        booking.setBookingId(uuid);
        booking.setAmountToBePaid(bill);
        bookingMap.put(uuid,booking);
        hotelMap.get(hotel).setAvailableRooms(hotelMap.get(hotel).getAvailableRooms()-booking.getNoOfRooms());
        return bill;
    }

    public int getBookings(Integer aadharCard) {
        int cnt=0;
        for (Booking booking:bookingMap.values()) {
            if (booking.getBookingAadharCard() == aadharCard) cnt++;
        }
        return cnt;
    }
}
