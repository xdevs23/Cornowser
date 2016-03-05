KEYPWD = $(read -s -p "Key password: ")
STOREPWD = $(read -s -p " Store password: ")

buildrel:
	./make.sh --gpw $(read -s -p "Key password: ") $(STOREPWD)
