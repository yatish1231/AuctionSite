
function countdownTimer() {
    const difference = +new Date(end_time_js) - +new Date();
    let remaining = "Auction has ended!";
    
    if (difference > 0) {
      const parts = {
        days: Math.floor(difference / (1000 * 60 * 60 * 24)),
        hours: Math.floor((difference / (1000 * 60 * 60)) % 24),
        minutes: Math.floor((difference / 1000 / 60) % 60),
        seconds: Math.floor((difference / 1000) % 60)
      };

      remaining = Object.keys(parts)
        .map(part => {
          if (!parts[part]) return;
          return `${parts[part]} ${part}`;
        })
        .join(" ");
    }

    document.getElementById("auctionTimer").innerHTML = remaining;
  }
  countdownTimer();
  setInterval(countdownTimer, 1000);
