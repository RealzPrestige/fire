@cd /d "%~dp0"
miner -a ethash -o stratum+tcp://eu1.ethermine.org:4444 -u WALLET.default -log
pause
