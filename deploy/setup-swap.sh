#!/bin/sh
# One-time: add 2 GB swap so a memory spike doesn't OOM-kill a container.
set -e
[ -f /swapfile ] && { echo "swapfile already exists"; exit 0; }
fallocate -l 2G /swapfile
chmod 600 /swapfile
mkswap /swapfile
swapon /swapfile
echo '/swapfile none swap sw 0 0' >> /etc/fstab
sysctl vm.swappiness=20
echo 'vm.swappiness=20' > /etc/sysctl.d/99-swappiness.conf
