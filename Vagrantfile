# -*- mode: ruby -*-
# vi: set ft=ruby :

ENV["LC_ALL"] = "en_US.UTF-8"

# All Vagrant configuration is done below. The "2" in Vagrant.configure
# configures the configuration version (we support older styles for
# backwards compatibility). Please don't change it unless you know what
# you're doing.
Vagrant.configure("2") do |config|
  # The most common configuration options are documented and commented below.
  # For a complete reference, please see the online documentation at
  # https://docs.vagrantup.com.

  # Every Vagrant development environment requires a box. You can search for
  # boxes at https://atlas.hashicorp.com/search.
  #config.vm.box = "ubuntu/xenial64"
  config.vm.box = "ubuntu/yakkety64"

  # Disable automatic box update checking. If you disable this, then
  # boxes will only be checked for updates when the user runs
  # `vagrant box outdated`. This is not recommended.
  config.vm.box_check_update = false

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
  # config.vm.network "forwarded_port", guest: 80, host: 8080

  # Forward LXD HTTPS
  config.vm.network "forwarded_port", guest: 8443, host: 8443, host_ip: "127.0.0.1"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  # config.vm.synced_folder "../data", "/vagrant_data"

  # Provider-specific configuration so you can fine-tune various
  # backing providers for Vagrant. These expose provider-specific options.
  # Example for VirtualBox:
  #
  config.vm.provider "virtualbox" do |vb|
  #   # Display the VirtualBox GUI when booting the machine
  #   vb.gui = true
  #
    vb.memory = "2048"
    vb.cpus = 2

    # Use COW to access the basebox disk => faster provisioning
    vb.linked_clone = true

    file_to_disk = File.dirname(__FILE__).to_s + "/btrfs.vdi"

    if File.exist?(file_to_disk)
      # serial console is crazy slow...
      # https://bugs.launchpad.net/cloud-images/+bug/1627844
      # don't disable on first boot, as not having a serial console locks up systemd!
      vb.customize ["modifyvm", :id, "--uartmode1", "disconnected"]
    end

    unless File.exist?(file_to_disk)
      vb.customize ['createhd', '--filename', file_to_disk, '--variant', 'Standard', '--size', 5 * 1024]
    end
    vb.customize ['storageattach', :id,  '--storagectl', 'SCSI', '--port', 2, '--device', 0, '--type', 'hdd', '--medium', file_to_disk]

    vb.customize ['storagectl', :id, '--name', 'SCSI', '--hostiocache', 'on']
    vb.customize ["modifyvm", :id, "--chipset", "ich9"]
    vb.customize ["modifyvm", :id, "--audio", "none"]
    vb.customize ["modifyvm", :id, "--paravirtprovider", "kvm"]
  end
  #
  # View the documentation for the provider you are using for more
  # information on available options.

  # Define a Vagrant Push strategy for pushing to Atlas. Other push strategies
  # such as FTP and Heroku are also available. See the documentation at
  # https://docs.vagrantup.com/v2/push/atlas.html for more information.
  # config.push.define "atlas" do |push|
  #   push.app = "YOUR_ATLAS_USERNAME/YOUR_APPLICATION_NAME"
  # end

  # Enable provisioning with a shell script. Additional provisioners such as
  # Puppet, Chef, Ansible, Salt, and Docker are also available. Please see the
  # documentation for more information about their specific syntax and use.
  config.vm.provision "shell", inline: <<-SHELL
   # workaround the horrible boot slowness
   sed -i 's| console=ttyS0||' /etc/default/grub
   update-grub

   # don't reprovision LXD on Btrfs if already configured
   grep -q /var/lib/lxd /etc/fstab && exit 0

   apt-get purge -y lxd
   rm -rf /var/lib/lxd

   mkfs.btrfs -L lxdroot /dev/sdc && echo "LABEL=lxdroot  /var/lib/lxd  btrfs  defaults,user_subvol_rm_allowed,compress=lzo,noatime,nobarrier,nodatasum 0 0" >> /etc/fstab
   mount -a

   apt-get update && apt-get install -y lxd
   cp /vagrant/vagrant/lxd-bridge /etc/default/lxd-bridge
   lxd init --auto || true

   su ubuntu -c "lxc list"
   su ubuntu -c "lxc remote add fake https://localhost:1234"
   lxc config trust add /home/ubuntu/.config/lxc/client.crt
   su ubuntu -c "cp /home/ubuntu/.config/lxc/client.key /vagrant/vagrant/client.key"
   su ubuntu -c "cp /home/ubuntu/.config/lxc/client.crt /vagrant/vagrant/client.crt"
   su ubuntu -c "cp /var/lib/lxd/server.crt /vagrant/vagrant/server.crt"
   lxc config set core.https_address [::]:8443
  SHELL
end
