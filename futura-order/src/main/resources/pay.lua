---
--- Redis Lua script for flash sale inventory deduction and user duplicate purchase prevention
---
local userExist = redis.call('EXISTS', KEYS[2])
if userExist == 1 then
    return -1
end

local stock = tonumber(redis.call('get', KEYS[1]) or 0)
local num = tonumber(ARGV[1])
if stock < num then
    return -2
end

redis.call('decrby', KEYS[1], num)
redis.call('set', KEYS[2], 1, 'EX', 60 * 60 * 24)
return 1
