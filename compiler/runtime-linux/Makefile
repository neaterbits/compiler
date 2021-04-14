
DEBUG_DIR := ./Debug

LIBLINUX_SO := $(DEBUG_DIR)/liblinuxjni.so



liblinux_bindings.c : com_neaterbits_runtime__native_NativeMethods.h
INCLUDES := \
		-I/usr/lib/jvm/java-8-openjdk-amd64/include/ \
		-I/usr/lib/jvm/java-8-openjdk-amd64/include/linux/

LIBS :=	-ldl

$(LIBLINUX_SO) : liblinux_bindings.c
	gcc -Wall $(INCLUDES) -fPIC -shared -o $@ $(LIBS)  $^


$(DEBUG_DIR) :
	mkdir $(DEBUG_DIR)

compile : $(DEBUG_DIR) $(LIBLINUX_SO)

clean :
	rm -r $(DEBUG_DIR)

install : compile


all : install


