package ru.yandex.practicum.filmorate.repository.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EqualsUsersIdException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private static final Map<Long, User> users = new HashMap<>();
    private static final Map<Long, Set<Long>> friends = new HashMap<>();

    private long generatorId = 0;

    private long generateId() {
        return ++generatorId;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Long id) {
        return users.get(id);
    }

    @Override
    public List<User> mutualFriends(Long userId, Long friendId) {
        equalsUsersId(userId, friendId);
        Set<Long> userFriendsId = getSetUser(userId);
        Set<Long> friendFriendsId = getSetUser(friendId);
        if (userFriendsId == null || friendFriendsId == null) {
            return new ArrayList<>();
        }
        List<User> mutualF = new ArrayList<>();
        for (Long uFId : userFriendsId) {
            for (Long fFId : friendFriendsId) {
                if (uFId == fFId) {
                    mutualF.add(users.get(uFId));
                    break;
                }
            }
        }
        return mutualF;
    }

    @Override
    public List<User> getFriendsUser(Long userId) {
        checkUser(userId);
        List<User> userFriends = new ArrayList<>();
        for (Long id : getSetUser(userId)) {
            userFriends.add(users.get(id));
        }
        return userFriends;
    }

    @Override
    public User postUsers(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User putUsers(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("UserId: " + user.getId());
        }
    }

    @Override
    public void delUsers() {
        users.clear();
        friends.clear();
    }

    @Override
    public User delUser(Long id) {
        Set<Long> friend = friends.get(id);
        friend.forEach(f -> deleteFriend(f, id));
        return users.remove(id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        equalsUsersId(userId, friendId);
        checkUser(userId);
        checkUser(friendId);
        if (users.get(userId) == null) {
            throw new NotFoundException("user userId: " + userId);
        }
        Set<Long> uFriendIds = friends.computeIfAbsent(userId, id -> new HashSet<>());
        uFriendIds.add(friendId);

        Set<Long> fFriendIds = friends.computeIfAbsent(friendId, id -> new HashSet<>());
        fFriendIds.add(userId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        equalsUsersId(userId, friendId);
        Set<Long> userFriendIds = getSetUser(userId);
        userFriendIds.remove(friendId);

        Set<Long> friendUserIds = getSetUser(friendId);
        friendUserIds.remove(userId);
    }

    private void equalsUsersId(Long userId, Long friendId) {
        if (userId == friendId) {
            throw new EqualsUsersIdException("The same user is specified");
        }
    }

    private Set<Long> getSetUser(Long userId) {
        Set<Long> userMap = friends.get(userId);
        if (userId == null) {
            throw new NotFoundException("userId: " + userId);
        } else {
            return userMap;
        }
    }

    private void checkUser(Long id) {
        if (users.get(id) == null) {
            throw new NotFoundException("user userId: " + id);
        }
    }
}
