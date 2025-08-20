#!/bin/bash

echo "=== Cache Performance Testing Script ==="
echo "This script will test the cache implementation by making HTTP requests"
echo

ACCOUNT_ID="ACC0000001"
BASE_URL="http://localhost:8081"
ENDPOINT="/accounts/${ACCOUNT_ID}/transactions"

echo "Testing endpoint: ${BASE_URL}${ENDPOINT}"
echo

make_request() {
    local request_num=$1
    echo "Request #${request_num}:"
    
    start_time=$(date +%s%3N)
    response=$(curl -s -w "%{http_code}" -o /tmp/response_${request_num}.json "${BASE_URL}${ENDPOINT}")
    end_time=$(date +%s%3N)
    
    duration=$((end_time - start_time))
    
    if [ "$response" = "200" ]; then
        transaction_count=$(cat /tmp/response_${request_num}.json | jq '. | length' 2>/dev/null || echo "unknown")
        echo "  ✅ Success (${duration}ms) - Transactions: ${transaction_count}"
    else
        echo "  ❌ Failed (HTTP ${response}) - Time: ${duration}ms"
    fi
    
    echo "  Response time: ${duration}ms"
    return $duration
}

echo "Making 5 consecutive requests to test cache performance..."
echo

times=()
for i in {1..5}; do
    make_request $i
    times+=($?)
    echo
    sleep 1
done

echo "=== Performance Analysis ==="
echo "Request times: ${times[@]}ms"

if [ ${#times[@]} -ge 2 ]; then
    first_time=${times[0]}
    second_time=${times[1]}
    
    if [ $first_time -gt 0 ]; then
        improvement=$(( (first_time - second_time) * 100 / first_time ))
        echo "Performance improvement from 1st to 2nd request: ${improvement}%"
        
        if [ $improvement -gt 90 ]; then
            echo "✅ Cache performance target achieved (>90% improvement)"
        elif [ $improvement -gt 50 ]; then
            echo "⚠️ Cache working but improvement below target"
        else
            echo "❌ Cache may not be working effectively"
        fi
    fi
fi

echo
echo "=== Test Complete ==="
